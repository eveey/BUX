package com.evastos.bux.data.domain.product.feed

import com.evastos.bux.TestUtil
import com.evastos.bux.data.exception.ExceptionMappers
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.connection.ConnectEventType
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.service.RtfService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductFeedRepositoryTest {

    private val service = mock<RtfService>()
    private val exceptionMapper = mock<ExceptionMappers.Rtf>()

    private lateinit var productFeedRepository: ProductFeedRepository

    @Before
    fun setUp() {
        productFeedRepository = ProductFeedRepository(exceptionMapper)
    }

    @Test
    fun observeSocketConnectionState_publishesDistinctConnectionStates() {
        val expectedEvents = listOf(
            TestUtil.webSocketConnectionOpened,
            TestUtil.webSocketConnectionClosed,
            TestUtil.webSocketConnectionFailed,
            TestUtil.webSocketMessageReceived,
            TestUtil.webSocketConnectionFailed,
            TestUtil.webSocketConnectionClosing)
        whenever(service.observeState()).thenReturn(
            Flowable.fromArray(
                TestUtil.webSocketConnectionOpened,
                TestUtil.webSocketConnectionClosed,
                TestUtil.webSocketConnectionClosed,
                TestUtil.webSocketConnectionClosed,
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketMessageReceived,
                TestUtil.webSocketMessageReceived,
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketConnectionClosing
            )
        )

        productFeedRepository.observeSocketConnectionState(service).toList()
                .subscribe { list: MutableList<WebSocket.Event>?, throwable: Throwable? ->
                    assertNull(throwable)
                    assertArrayEquals(expectedEvents.toTypedArray(), list?.toTypedArray())
                }
    }

    @Test
    fun observeSocketConnectionState_withOnlyFailedEvents_publishesOneConnectionFailedEvent() {
        val expectedEvents = listOf(
            TestUtil.webSocketConnectionFailed)
        whenever(service.observeState()).thenReturn(
            Flowable.fromArray(
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketConnectionFailed,
                TestUtil.webSocketConnectionFailed
            )
        )

        productFeedRepository.observeSocketConnectionState(service).toList()
                .subscribe { list: MutableList<WebSocket.Event>?, throwable: Throwable? ->
                    assertNull(throwable)
                    assertArrayEquals(expectedEvents.toTypedArray(), list?.toTypedArray())
                }
    }

    @Test
    fun subscribeToFeed_whenConnected_whenSubscribed_withTradingProductEvent_publishesEvent() {
        whenever(service.connect()).thenReturn(
            Flowable.just(ConnectEvent(ConnectEventType.CONNECTED, null))
        )
        whenever(service.sendSubscribe(any())).thenReturn(true)
        whenever(service.observeUpdates()).thenReturn(Flowable.just(TestUtil.updateEvent))

        productFeedRepository.subscribeToFeed(service, "").toList()
                .subscribe { updateEventList, throwable ->
                    assertNull(throwable)
                    assertEquals(1, updateEventList.size)
                    assertEquals(TestUtil.updateEvent, updateEventList[0])
                }
    }

    @Test
    fun subscribeToFeed_whenConnected_whenSubscribed_publishesOnlyTradingProductEvents() {
        whenever(service.connect()).thenReturn(
            Flowable.just(ConnectEvent(ConnectEventType.CONNECTED, null))
        )
        whenever(service.sendSubscribe(any())).thenReturn(true)
        whenever(service.observeUpdates()).thenReturn(
            Flowable.fromArray(
                TestUtil.getUpdateEvent(Channel.UNKNOWN),
                TestUtil.getUpdateEvent(Channel.TRADING_QUOTE),
                TestUtil.getUpdateEvent(Channel.UNKNOWN),
                TestUtil.getUpdateEvent(Channel.UNKNOWN),
                TestUtil.getUpdateEvent(Channel.UNKNOWN),
                TestUtil.getUpdateEvent(Channel.TRADING_QUOTE),
                TestUtil.getUpdateEvent(Channel.UNKNOWN)
            )
        )

        productFeedRepository.subscribeToFeed(service, "").toList()
                .subscribe { updateEventList, throwable ->
                    assertNull(throwable)
                    assertEquals(2, updateEventList.size)
                    updateEventList.forEach {
                        assertEquals(Channel.TRADING_QUOTE, it.channel)
                    }
                }
    }

    @Test
    fun subscribeToFeed_whenNotConnected_publishesException() {
        whenever(exceptionMapper.map(any())).thenReturn(RtfException.NotConnectedException())
        whenever(service.connect()).thenReturn(
            Flowable.fromArray(
                ConnectEvent(null, null),
                ConnectEvent(null, null),
                ConnectEvent(ConnectEventType.CONNECT_FAILED, null),
                ConnectEvent(ConnectEventType.CONNECTED, null)
            )
        )
        whenever(service.sendSubscribe(any())).thenReturn(true)
        whenever(service.observeUpdates()).thenReturn(Flowable.just(TestUtil.updateEvent))

        productFeedRepository.subscribeToFeed(service, "").toList()
                .subscribe { updateEventList, throwable ->
                    assertTrue(throwable is RtfException.NotConnectedException)
                    assertTrue(updateEventList.isEmpty())
                }
    }

    @Test
    fun subscribeToFeed_whenConnected_doesNotPublishException() {
        whenever(exceptionMapper.map(any())).thenReturn(RtfException.NotConnectedException())
        whenever(service.connect()).thenReturn(
            Flowable.fromArray(
                ConnectEvent(null, null),
                ConnectEvent(null, null),
                ConnectEvent(null, null),
                ConnectEvent(ConnectEventType.CONNECTED, null)
            )
        )
        whenever(service.sendSubscribe(any())).thenReturn(true)
        whenever(service.observeUpdates()).thenReturn(Flowable.just(TestUtil.updateEvent))

        productFeedRepository.subscribeToFeed(service, "").toList()
                .subscribe { updateEventList, throwable ->
                    assertNull(throwable)
                    assertTrue(updateEventList.isEmpty())
                }
    }

    @Test
    fun subscribeToFeed_whenConnected_whenNotSubscribed_publishesException() {
        whenever(exceptionMapper.map(any())).thenReturn(RtfException.NotSubscribedException())
        whenever(service.connect()).thenReturn(
            Flowable.just(ConnectEvent(ConnectEventType.CONNECTED, null))
        )
        whenever(service.sendSubscribe(any())).thenReturn(false)
        whenever(service.observeUpdates()).thenReturn(Flowable.just(TestUtil.updateEvent))

        productFeedRepository.subscribeToFeed(service, "").toList()
                .subscribe { updateEventList, throwable ->
                    assertTrue(throwable is RtfException.NotSubscribedException)
                    assertTrue(updateEventList.isEmpty())
                }
    }
}
