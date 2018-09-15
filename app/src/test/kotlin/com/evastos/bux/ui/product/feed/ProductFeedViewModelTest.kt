package com.evastos.bux.ui.product.feed

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.bux.RxTestSchedulerRule
import com.evastos.bux.TestUtil
import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.api.response.ProductPrice
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.model.rtf.update.UpdateEventBody
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.NumberUtil
import com.evastos.bux.ui.util.PriceUtil
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class ProductFeedViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxTestSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val repository = mock<Repositories.ProductFeedRepository>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.Rtf>()
    private val tradingProductNameLiveDataObserver = mock<Observer<String>>()
    private val previousDayClosingPriceLiveDataObserver = mock<Observer<String>>()
    private val currentPriceLiveDataObserver = mock<Observer<String>>()
    private val priceDifferenceLiveDataObserver = mock<Observer<String>>()
    private val exceptionLiveDataObserver = mock<Observer<String?>>()
    private val lastUpdatedLiveDataObserver = mock<Observer<String>>()

    private val rtfService = mock<RtfService>()
    private val productDetails = ProductDetails(
        symbol = "USD",
        securityId = "sb27639",
        displayName = "US dollar",
        closingPrice = ProductPrice("@", 10, BigDecimal.valueOf(1.2345)),
        currentPrice = ProductPrice("$", 4, BigDecimal.valueOf(2.837))
    )
    private val updateEvent = UpdateEvent(
        channel = Channel.TRADING_QUOTE,
        body = UpdateEventBody(
            securityId = "sb27639",
            currentPrice = BigDecimal.valueOf(5.678)
        )
    )

    private var dateTimeNow = "12/09/2018 9:24 PM CEST"

    private lateinit var viewModel: ProductFeedViewModel

    @Before
    fun setUp() {
        viewModel = ProductFeedViewModel(
            repository,
            exceptionMessageProvider,
            TestDateTimeUtil(),
            TestPriceUtil(),
            TestUtil.rxSchedulers
        )
        whenever(repository.subscribeToFeed(any(), any(), anyOrNull())).thenReturn(
            Flowable.just(updateEvent)
        )
        whenever(repository.observeSocketConnectionState(any())).thenReturn(
            Flowable.just(
                com.tinder.scarlet.WebSocket.Event.OnConnectionClosed(ShutdownReason.GRACEFUL))
        )

        viewModel.tradingProductNameLiveData
                .observeForever(tradingProductNameLiveDataObserver)
        viewModel.previousDayClosingPriceLiveData
                .observeForever(previousDayClosingPriceLiveDataObserver)
        viewModel.currentPriceLiveData
                .observeForever(currentPriceLiveDataObserver)
        viewModel.priceDifferenceLiveData
                .observeForever(priceDifferenceLiveDataObserver)
        viewModel.exceptionLiveData
                .observeForever(exceptionLiveDataObserver)
        viewModel.lastUpdatedLiveData
                .observeForever(lastUpdatedLiveDataObserver)
    }

    @Test
    fun getTradingProductNameLiveData() {
        val tradingName = productDetails.displayName
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(tradingProductNameLiveDataObserver).onChanged(tradingName)
    }

    @Test
    fun getPreviousDayClosingPriceLiveData() {
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(previousDayClosingPriceLiveDataObserver).onChanged("1.2345@10")
    }

    @Test
    fun getCurrentPriceLiveData_withSuccessfulUpdate_shouldShowInitialAndUpdatedData() {
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(currentPriceLiveDataObserver).onChanged("2.837$4")
        verify(currentPriceLiveDataObserver).onChanged("5.678$4")
    }

    @Test
    fun getCurrentPriceLiveData_withError_shouldShowOnlyInitialData() {
        whenever(repository.subscribeToFeed(any(), any(), anyOrNull()))
                .thenReturn(Flowable.error(Throwable()))

        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(currentPriceLiveDataObserver).onChanged("2.837$4")
        verifyNoMoreInteractions(currentPriceLiveDataObserver)
    }

    @Test
    fun getPriceDifferenceLiveData_withSuccessfulUpdate_shouldShowInitialAndUpdatedData() {
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(priceDifferenceLiveDataObserver).onChanged("1.2345/2.837")
        verify(priceDifferenceLiveDataObserver).onChanged("1.2345/5.678")
    }

    @Test
    fun getPriceDifferenceLiveDataa_withError_shouldShowOnlyInitialData() {
        whenever(repository.subscribeToFeed(any(), any(), anyOrNull()))
                .thenReturn(Flowable.error(Throwable()))

        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(priceDifferenceLiveDataObserver).onChanged("1.2345/2.837")
        verifyNoMoreInteractions(priceDifferenceLiveDataObserver)
    }

    @Test
    fun getExceptionLiveData_withErrorSubscribeToFeed_shouldPostMessage() {
        val throwable = Throwable()
        val exceptionMessage = "exception"
        whenever(repository.subscribeToFeed(any(), any(), anyOrNull()))
                .thenReturn(Flowable.error(throwable))
        whenever(exceptionMessageProvider.getMessage(throwable)).thenReturn(exceptionMessage)

        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(exceptionLiveDataObserver).onChanged(exceptionMessage)
    }

    @Test
    fun getExceptionLiveData_withConnectionFailed_shouldPostMessage() {
        val exceptionMessage = "exception"
        whenever(repository.observeSocketConnectionState(any()))
                .thenReturn(Flowable.just(WebSocket.Event.OnConnectionFailed(Throwable())))
        whenever(exceptionMessageProvider.getMessage(any())).thenReturn(exceptionMessage)

        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(exceptionLiveDataObserver).onChanged(exceptionMessage)
    }

    @Test
    fun getExceptionLiveData_withConnectionNotFailed_shouldClearMessage() {
        val exceptionMessage = "exception"
        whenever(repository.observeSocketConnectionState(any())).thenReturn(
            Flowable.just(WebSocket.Event.OnConnectionClosing(ShutdownReason.GRACEFUL))
        )
        whenever(exceptionMessageProvider.getMessage(any())).thenReturn(exceptionMessage)

        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(exceptionLiveDataObserver).onChanged(null)
    }

    @Test
    fun getLastUpdatedLiveData() {
        verify(lastUpdatedLiveDataObserver).onChanged(dateTimeNow)
    }

    @Test
    fun subscribeToProductFeed_shouldSubscribeToProductFeed() {
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(repository).subscribeToFeed(rtfService, "sb27639")
    }

    @Test
    fun subscribeToProductFeed_shouldObserveSocketConnectionState() {
        viewModel.subscribeToProductFeed(productDetails, rtfService)

        verify(repository).observeSocketConnectionState(rtfService)
    }

    @Test
    fun retrySubscribe_shouldRepeatLastCall() {
        val productDetails1 = ProductDetails(
            "",
            "securityId1",
            "",
            null,
            null
        )
        val rtfService1 = mock<RtfService>()
        viewModel.subscribeToProductFeed(productDetails1, rtfService1)
        val productDetails2 = ProductDetails(
            "",
            "securityId2",
            "",
            null,
            null)
        val rtfService2 = mock<RtfService>()
        viewModel.subscribeToProductFeed(productDetails2, rtfService2)
        val rtfService3 = mock<RtfService>()

        viewModel.retrySubscribe(rtfService3)

        verify(repository).subscribeToFeed(rtfService1, "securityId1")
        verify(repository).observeSocketConnectionState(rtfService1)
        verify(repository).subscribeToFeed(rtfService2, "securityId2")
        verify(repository).observeSocketConnectionState(rtfService2)
        verify(repository).subscribeToFeed(rtfService3, "securityId2")
        verify(repository).observeSocketConnectionState(rtfService3)
    }

    private inner class TestDateTimeUtil : DateTimeUtil() {
        override fun getTimeNow(): String {
            return dateTimeNow
        }
    }

    private inner class TestNumberUtil : NumberUtil() {
        override fun getPercentDifference(
            previousNumber: BigDecimal,
            currentNumber: BigDecimal
        ): BigDecimal {
            return BigDecimal.ZERO
        }
    }

    private inner class TestPriceUtil : PriceUtil(TestNumberUtil()) {
        override fun getLocalisedPrice(
            price: BigDecimal?,
            currencyCode: String?,
            decimalPlaces: Int?
        ): String {
            return "${price?.toPlainString()}$currencyCode$decimalPlaces"
        }

        override fun getPriceDifferencePercent(
            previousPrice: BigDecimal?,
            currentPrice: BigDecimal?
        ): String {
            return "${previousPrice?.toPlainString()}/${currentPrice?.toPlainString()}"
        }
    }
}