package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.MutableLiveData
import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.exception.rtf.RtfExceptionMessageProvider
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.base.BaseViewModel
import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.PriceUtil
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel
@Inject constructor(
    private val productFeedRepository: Repositories.ProductFeedRepository,
    private val scarletBuilder: Scarlet.Builder,
    private val exceptionMessageProvider: RtfExceptionMessageProvider,
    private val dateTimeUtil: DateTimeUtil,
    private val priceUtil: PriceUtil,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    val tradingProductNameLiveData = MutableLiveData<String>()
    val previousDayClosingPriceLiveData = MutableLiveData<String>()
    val currentPriceLiveData = MutableLiveData<String>()
    val priceDifferenceLiveData = MutableLiveData<String>()
    val exceptionLiveData = MutableLiveData<String?>()
    val lastUpdatedLiveData = MutableLiveData<String>()

    private lateinit var productDetails: ProductDetails

    init {
        lastUpdatedLiveData.postValue(dateTimeUtil.getTimeNow())
    }

    fun subscribeToProductFeed(productDetails: ProductDetails, lifecycle: Lifecycle) {
        this.productDetails = productDetails
        tradingProductNameLiveData.postValue(productDetails.displayName)

        with(productDetails.closingPrice) {
            val previousDayClosingPrice = priceUtil.getLocalisedPrice(
                this?.amount,
                this?.currency,
                this?.decimals
            )
            previousDayClosingPriceLiveData.postValue(previousDayClosingPrice)
        }

        with(productDetails.currentPrice) {
            val currentPrice = priceUtil.getLocalisedPrice(
                this?.amount,
                this?.currency,
                this?.decimals
            )
            currentPriceLiveData.postValue(currentPrice)
        }

        val priceDifference = priceUtil.getPriceDifferencePercent(
            productDetails.closingPrice?.amount,
            productDetails.currentPrice?.amount
        )
        priceDifferenceLiveData.postValue(priceDifference)

        // keep connection alive during the activity lifecycle instead of application lifecycle
        val rtfService = scarletBuilder.lifecycle(lifecycle).build().create<RtfService>()
        subscribeToProduct(rtfService)
    }

    fun retrySubscribe(lifecycle: Lifecycle) {
        val rtfService = scarletBuilder.lifecycle(lifecycle).build().create<RtfService>()
        subscribeToProduct(rtfService)
    }

    private fun subscribeToProduct(rtfService: RtfService) {
        disposables.clear()
        disposables.add(
            productFeedRepository
                    .subscribeToFeed(rtfService, productDetails.securityId)
                    .applySchedulers(rxSchedulers)
                    .subscribe({ event ->
                        lastUpdatedLiveData.value = dateTimeUtil.getTimeNow()
                        Timber.i(event.toString())
                        currentPriceLiveData.value = getCurrentPrice(productDetails, event)
                        priceDifferenceLiveData.value = getPriceDifference(productDetails, event)
                    }, { throwable ->
                        exceptionLiveData.value = exceptionMessageProvider.getMessage(throwable)
                        Timber.e(throwable)
                    })
        )

        disposables.add(productFeedRepository.observeSocketConnectionState(rtfService)
                .applySchedulers(rxSchedulers)
                .subscribe({
                    if (it is WebSocket.Event.OnConnectionFailed) {
                        exceptionLiveData.value =
                                exceptionMessageProvider.getMessage(RtfException.ServerException())
                    } else {
                        exceptionLiveData.value = null
                    }
                }, { Timber.e(it) }))
    }

    private fun getCurrentPrice(productDetails: ProductDetails, updateEvent: UpdateEvent): String {
        return priceUtil.getLocalisedPrice(
            updateEvent.body.currentPrice,
            productDetails.currentPrice?.currency,
            productDetails.currentPrice?.decimals)
    }

    private fun getPriceDifference(
        productDetails: ProductDetails,
        updateEvent: UpdateEvent
    ): String {
        return priceUtil.getPriceDifferencePercent(
            productDetails.closingPrice?.amount,
            updateEvent.body.currentPrice)
    }
}
