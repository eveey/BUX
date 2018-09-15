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

    private val repository = mock<Repositories.ProductDetailsRepository>()
    private val exceptionMessageProvider = mock<ExceptionMessageProviders.Api>()
    private val productDetailsLiveDataObserver = mock<Observer<ProductDetails>>()
    private val errorMessageLiveDataObserver = mock<Observer<String>>()
    private val progressVisibleLiveDataObserver = mock<Observer<Boolean>>()

    private val productDetails = TestUtil.productDetails

    private lateinit var viewModel: ProductIdentifierViewModel

    @Before
    fun setUp() {
        viewModel = ProductIdentifierViewModel(
            repository,
            exceptionMessageProvider,
            TestUtil.rxSchedulers
        )
        viewModel.productDetailsLiveData.observeForever(productDetailsLiveDataObserver)
        viewModel.errorMessageLiveData.observeForever(errorMessageLiveDataObserver)
        viewModel.progressVisibleLiveData.observeForever(progressVisibleLiveDataObserver)
    }

    @Test
    fun getProductDetailsLiveData_withSuccess_postsProductDetails() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        viewModel.getProductDetails("sb26000")

        verify(productDetailsLiveDataObserver).onChanged(check {
            assertEquals(productDetails, it)
        })
    }

    @Test
    fun getProductDetailsLiveData_withError_doesNothing() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.error(Throwable()))

        viewModel.getProductDetails("sb26000")

        verify(productDetailsLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun getErrorMessageLiveData_withError_postsErrorMessage() {
        val errorMessage = "exception"
        val throwable = Throwable()
        whenever(exceptionMessageProvider.getMessage(throwable)).thenReturn(errorMessage)
        whenever(repository.getProductDetails(any())).thenReturn(Single.error(throwable))

        viewModel.getProductDetails("sb26000")

        verify(errorMessageLiveDataObserver).onChanged(errorMessage)
    }

    @Test
    fun getProductDetailsLiveData_withSuccess_doesNothing() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        viewModel.getProductDetails("sb26000")

        verify(errorMessageLiveDataObserver, never()).onChanged(any())
    }

    @Test
    fun getProgressVisibleLiveData_withSuccess_postsTrueThenFalse() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.just(productDetails))

        viewModel.getProductDetails("sb26000")

        verify(progressVisibleLiveDataObserver).onChanged(true)
        verify(progressVisibleLiveDataObserver).onChanged(false)
    }

    @Test
    fun getProgressVisibleLiveData_withError_postsTrueThenFalse() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.error(Throwable()))

        viewModel.getProductDetails("sb26000")

        verify(progressVisibleLiveDataObserver).onChanged(true)
        verify(progressVisibleLiveDataObserver).onChanged(false)
    }

    @Test
    fun getProductDetails_getsProductDetailsForIdentifier() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.just(productDetails))
        val identifier = "sb26000"

        viewModel.getProductDetails(identifier)

        verify(repository).getProductDetails(identifier)
    }

    @Test
    fun retryGetProductDetails_getsProductDetailsForLastIdentifier() {
        whenever(repository.getProductDetails(any())).thenReturn(Single.just(productDetails))
        val identifier1 = "sb26000"
        viewModel.getProductDetails(identifier1)
        val identifier2 = "mn28379"
        viewModel.getProductDetails(identifier2)

        viewModel.retryGetProductDetails()

        verify(repository).getProductDetails(identifier1)
        verify(repository, times(2)).getProductDetails(identifier2)
    }
}
