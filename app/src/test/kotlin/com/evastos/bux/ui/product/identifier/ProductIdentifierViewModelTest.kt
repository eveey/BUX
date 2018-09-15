package com.evastos.bux.ui.product.identifier

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.bux.RxTestSchedulerRule
import com.evastos.bux.TestUtil
import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.check
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductIdentifierViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxTestSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val productDetailsRepository = mock<Repositories.ProductDetailsRepository>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.Api>()
    private val productDetailsLiveDataObserver = mock<Observer<ProductDetails>>()
    private val errorMessageLiveDataObserver = mock<Observer<String>>()
    private val progressVisibleLiveDataObserver = mock<Observer<Boolean>>()

    private val productDetails = ProductDetails("", "", "", null, null)

    private lateinit var productIdentifierViewModel: ProductIdentifierViewModel

    @Before
    fun setUp() {
        productIdentifierViewModel = ProductIdentifierViewModel(
            productDetailsRepository,
            exceptionMessageProvider,
            TestUtil.rxSchedulers
        )
        productIdentifierViewModel.productDetailsLiveData.observeForever(productDetailsLiveDataObserver)
        productIdentifierViewModel.errorMessageLiveData.observeForever(errorMessageLiveDataObserver)
        productIdentifierViewModel.progressVisibleLiveData.observeForever(progressVisibleLiveDataObserver)
    }

    @Test
    fun getProductDetailsLiveData_withSuccess_shouldPostProductDetails() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(productDetailsLiveDataObserver).onChanged(check {
            assertEquals(productDetails, it)
        })
    }

    @Test
    fun getProductDetailsLiveData_withError_shouldDoNothing() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.error(Throwable()))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(productDetailsLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun getErrorMessageLiveData_withError_shouldPostErrorMessage() {
        val errorMessage = "exception"
        val throwable = Throwable()
        whenever(exceptionMessageProvider.getMessage(throwable)).thenReturn(errorMessage)
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.error(throwable))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(errorMessageLiveDataObserver).onChanged(errorMessage)
    }

    @Test
    fun getProductDetailsLiveData_withSuccess_shouldDoNothing() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(errorMessageLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun getProgressVisibleLiveData_withSuccess_shouldPostTrueThenFalse() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(progressVisibleLiveDataObserver).onChanged(true)
        verify(progressVisibleLiveDataObserver).onChanged(false)
    }

    @Test
    fun getProgressVisibleLiveData_withError_shouldPostTrueThenFalse() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.error(Throwable()))

        productIdentifierViewModel.getProductDetails("sb26000")

        verify(progressVisibleLiveDataObserver).onChanged(true)
        verify(progressVisibleLiveDataObserver).onChanged(false)
    }

    @Test
    fun getProductDetails_shouldGetProductDetailsForIdentifier() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.just(productDetails))
        val identifier = "sb26000"

        productIdentifierViewModel.getProductDetails(identifier)

        verify(productDetailsRepository).getProductDetails(identifier)
    }

    @Test
    fun retryGetProductDetails_shouldGetProductDetailsForLastIdentifier() {
        whenever(productDetailsRepository.getProductDetails(any())).thenReturn(Single.just(productDetails))
        val identifier1 = "sb26000"
        productIdentifierViewModel.getProductDetails(identifier1)
        val identifier2 = "mn28379"
        productIdentifierViewModel.getProductDetails(identifier2)

        productIdentifierViewModel.retryGetProductDetails()

        verify(productDetailsRepository).getProductDetails(identifier1)
        verify(productDetailsRepository, times(2)).getProductDetails(identifier2)
    }
}