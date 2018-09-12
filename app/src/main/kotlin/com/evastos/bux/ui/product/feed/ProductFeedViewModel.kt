package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.MutableLiveData
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.exception.rtf.RtfException.NotConnectedException
import com.evastos.bux.data.exception.rtf.RtfException.NotSubscribedException
import com.evastos.bux.data.interactor.Repositories
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.rtf.connection.ConnectEventType.CONNECTED
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEventBody
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.data.rx.throttleLastMillis
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.base.BaseViewModel
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel
@Inject constructor(
    private val productFeedRepository: Repositories.ProductFeedRepository,
    private val scarletBuilder: Scarlet.Builder,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    companion object {
        private const val THROTTLE_INTERVAL = 500L
    }

    private lateinit var productFeedRetry: (() -> Unit)

    val tradingProductLiveData = MutableLiveData<ProductDetails>()

    val productFeedUpdateLiveData = MutableLiveData<UpdateEventBody>()

    val productFeedExceptionLiveData = MutableLiveData<RtfException>()

    fun subscribeToProductFeed(productDetails: ProductDetails, lifecycle: Lifecycle) {
        tradingProductLiveData.value = productDetails

        // we need to build RtfService with reference to the Activity lifecycle instead of Application lifecycle
        val rtfService = scarletBuilder.lifecycle(lifecycle).build().create<RtfService>()
        subscribeToProduct(rtfService, productDetails.securityId)
        productFeedRetry = { subscribeToProduct(rtfService, productDetails.securityId) }
    }

    fun retrySubscribe() {
        productFeedRetry.invoke()
    }

    private fun subscribeToProduct(rtfService: RtfService, productIdentifier: String) {
        disposables.add(
            connect(rtfService)
                    .flatMapPublisher { connectEvent ->
                        return@flatMapPublisher when (connectEvent.eventType) {
                            CONNECTED -> {
                                productFeedRepository.subscribeToChannel(
                                    rtfService,
                                    productIdentifier
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
                    }.filter {
                        Timber.d(it.toString())
                        it.channel == Channel.TRADING_QUOTE
                    }.throttleLastMillis(THROTTLE_INTERVAL)
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.i(updateEvent.toString())
                        if (updateEvent.body != null) {
                            productFeedUpdateLiveData.postValue(updateEvent.body)
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
