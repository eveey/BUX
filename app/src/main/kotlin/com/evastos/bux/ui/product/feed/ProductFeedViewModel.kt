package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.MutableLiveData
import com.evastos.bux.data.exception.rtf.RtfExceptionMessageProvider
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.repository.Repositories
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.base.BaseViewModel
import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.PriceUtil
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel
@Inject constructor(
    private val productFeedRepository: Repositories.ProductFeedRepository,
    private val exceptionMessageProvider: RtfExceptionMessageProvider,
    private val scarletBuilder: Scarlet.Builder,
    private val dateTimeUtil: DateTimeUtil,
    private val priceUtil: PriceUtil,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    val tradingProductNameLiveData = MutableLiveData<String>()
    val previousDayClosingPriceLiveData = MutableLiveData<String>()
    val currentPriceLiveData = MutableLiveData<String>()
    val priceDifferenceLiveData = MutableLiveData<String>()
    val exceptionLiveData = MutableLiveData<String>()
    val lastUpdatedLiveData = MutableLiveData<String>()

    private lateinit var productFeedRetry: (() -> Unit)

    fun subscribeToProductFeed(productDetails: ProductDetails, lifecycle: Lifecycle) {
        tradingProductNameLiveData.postValue(productDetails.displayName)
        lastUpdatedLiveData.postValue(dateTimeUtil.getTimeNow())

        with(productDetails.closingPrice) {
            val previousDayClosingPrice = priceUtil.getLocalisedPrice(this?.amount, this?.currency, this?.decimals)
            previousDayClosingPriceLiveData.postValue(previousDayClosingPrice)
        }

        with(productDetails.currentPrice) {
            val currentPrice = priceUtil.getLocalisedPrice(this?.amount, this?.currency, this?.decimals)
            currentPriceLiveData.postValue(currentPrice)
        }

        val priceDifference =
                priceUtil.getPriceDifferencePercent(
                    productDetails.closingPrice?.amount,
                    productDetails.currentPrice?.amount)
        priceDifferenceLiveData.postValue(priceDifference)

        // keep connection alive during the activity lifecycle instead of application lifecycle
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
            productFeedRepository
                    .subscribeToFeed(rtfService, productDetails.securityId)
                    .doOnNext {
                        lastUpdatedLiveData.postValue(dateTimeUtil.getTimeNow())
                    }
                    .distinctUntilChanged()
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.i(updateEvent.toString())
                        val currentPrice =
                                priceUtil.getLocalisedPrice(
                                    updateEvent.body.currentPrice,
                                    productDetails.currentPrice?.currency,
                                    productDetails.currentPrice?.decimals)
                        currentPriceLiveData.value = currentPrice
                        val priceDifference =
                                priceUtil.getPriceDifferencePercent(
                                    productDetails.closingPrice?.amount,
                                    updateEvent.body.currentPrice)
                        priceDifferenceLiveData.value = priceDifference
                    }, { throwable ->
                        exceptionLiveData.value = exceptionMessageProvider.getMessage(throwable)
                        Timber.e(throwable)
                    })
        )
    }
}
