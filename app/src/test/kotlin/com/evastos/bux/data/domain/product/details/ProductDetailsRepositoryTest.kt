package com.evastos.bux.data.domain.product.details

import com.evastos.bux.TestUtil
import com.evastos.bux.data.exception.ExceptionMappers
import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.service.ApiService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductDetailsRepositoryTest {

    private val service = mock<ApiService>()
    private val exceptionMapper = mock<ExceptionMappers.Api>()

    private lateinit var productDetailsRepository: ProductDetailsRepository

    @Before
    fun setUp() {
        productDetailsRepository = ProductDetailsRepository(service, exceptionMapper)
        whenever(service.getProductDetails(any()))
                .thenReturn(Single.just(TestUtil.productDetails))
    }

    @Test
    fun getProductDetails_withIdentifier_callsServiceWithIdentifier() {
        val productId = "sb27634"

        productDetailsRepository.getProductDetails(productId)

        verify(service).getProductDetails(productId)
    }

    @Test
    fun getProductDetails_withSuccess_getsProductDetails() {
        productDetailsRepository.getProductDetails("")
                .subscribeBy(onSuccess = {
                    assertEquals(TestUtil.productDetails, it)
                }, onError = {
                    assertNull(it)
                })
    }

    @Test
    fun getProductDetails_withError_throwsMappedException() {
        whenever(exceptionMapper.map(any())).thenReturn(ApiException.ServerException())
        whenever(service.getProductDetails(any())).thenReturn(Single.error(Throwable()))

        productDetailsRepository.getProductDetails("")
                .subscribeBy(onSuccess = { }, onError = {
                    assertTrue(it is ApiException.ServerException)
                })
    }
}
