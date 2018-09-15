package com.evastos.bux.ui.product.identifier

import com.evastos.bux.RxTestSchedulerRule
import com.evastos.bux.TestUtil
import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductIdentifierViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxTestSchedulerRule()

    private val productDetailsRepository = mock<Repositories.ProductDetailsRepository>()

    private val apiExceptionMessageProvider = mock<ExceptionMessageProviders.Api>()

    private lateinit var productIdentifierViewModel: ProductIdentifierViewModel

    @Before
    fun setUp() {
        productIdentifierViewModel = ProductIdentifierViewModel(
            productDetailsRepository,
            apiExceptionMessageProvider,
            TestUtil.rxSchedulers
        )
    }

    @Test
    fun getProductDetailsLiveData() {
    }

    @Test
    fun getErrorMessageLiveData() {
    }

    @Test
    fun getProgressVisibleLiveData() {
    }

    @Test
    fun getProductDetails() {
    }

    @Test
    fun retryGetProductDetails() {
    }
}