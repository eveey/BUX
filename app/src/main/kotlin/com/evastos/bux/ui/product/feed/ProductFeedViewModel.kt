package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.MutableLiveData
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.exception.rtf.RtfException.NotConnectedException
import com.evastos.bux.data.exception.rtf.RtfException.NotSubscribedException
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.rtf.connection.ConnectEventType.CONNECTED
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.repository.Repositories
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.base.BaseViewModel
import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.PriceUtil
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel
@Inject constructor(
    private val productFeedRepository: Repositories.ProductFeedRepository,
    private val scarletBuilder: Scarlet.Builder,
    private val dateTimeUtil: DateTimeUtil,
    private val priceUtil: PriceUtil,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    val tradingProductNameLiveData = MutableLiveData<String>()

    val previousDayClosingPriceLiveData = MutableLiveData<String>()

    val currentPriceLiveData = MutableLiveData<String>()

    val priceDifferenceLiveData = MutableLiveData<String>()

    val productFeedExceptionLiveData = MutableLiveData<RtfException>()

    val lastUpdatedLiveData = MutableLiveData<String>()

    private lateinit var productFeedRetry: (() -> Unit)

    fun subscribeToProductFeed(productDetails: ProductDetails, lifecycle: Lifecycle) {
        tradingProductNameLiveData.value = productDetails.displayName
        lastUpdatedLiveData.value = dateTimeUtil.getTimeNow()

        with(productDetails.closingPrice) {
            val previousDayClosingPrice = priceUtil.getLocalisedPrice(this?.amount, this?.currency, this?.decimals)
            previousDayClosingPriceLiveData.value = previousDayClosingPrice
        }

        with(productDetails.currentPrice) {
            val currentPrice = priceUtil.getLocalisedPrice(this?.amount, this?.currency, this?.decimals)
            currentPriceLiveData.value = currentPrice
        }

        // keep connection alive during this Activity lifecycle instead of Application lifecycle
        val rtfService = scarletBuilder.lifecycle(lifecycle).build().create<RtfService>()
        subscribeToProduct(rtfService, productDetails)
        productFeedRetry = { subscribeToProduct(rtfService, productDetails) }
    }

    fun retrySubscribeToProductFeed() {
        productFeedRetry.invoke()
    }

    private fun subscribeToProduct(rtfService: RtfService, productDetails: ProductDetails) {
        disposables.clear()
        disposables.add(
            connect(rtfService)
                    .flatMapPublisher { connectEvent ->
                        return@flatMapPublisher when (connectEvent.eventType) {
                            CONNECTED -> {
                                productFeedRepository.subscribeToChannel(
                                    rtfService,
                                    productDetails.securityId
                                ).let { subscribed ->
                                    if (subscribed) {
                                        productFeedRepository.observeUpdates(rtfService)
                                    } else {
                                        Flowable.error(NotSubscribedException())
                                    }
                                }
                            }
                            else -> Flowable.error(NotConnectedException())
                        }
                    }
                    .filter {
                        it.channel == Channel.TRADING_QUOTE
                    }
                    .doOnNext {
                        lastUpdatedLiveData.postValue(dateTimeUtil.getTimeNow())
                    }
                    .distinctUntilChanged()
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.i(updateEvent.toString())
                        if (updateEvent.body != null) {
                            val currentPrice = priceUtil.getLocalisedPrice(updateEvent.body.currentPrice, productDetails.currentPrice?.currency, productDetails.currentPrice?.decimals)
                            currentPriceLiveData.postValue(currentPrice)
                        } else {
                            productFeedExceptionLiveData.postValue(RtfException.UnknownException())
                        }
                    }, { throwable ->
                        if (throwable is RtfException) {
                            productFeedExceptionLiveData.postValue(throwable)
                        } else {
                            productFeedExceptionLiveData.postValue(RtfException.UnknownException())
                        }
                        Timber.e(throwable)
                    }))
    }

    private fun connect(rtfService: RtfService) =
            productFeedRepository.connect(rtfService)
                    .doOnNext {
                        Timber.d(it.toString())
                    }
                    .skipWhile {
                        it.eventType == null
                    }
                    .firstOrError()

}
