package com.evastos.bux.data.service

import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface RtfService {

    @Receive
    fun observeState(): Flowable<WebSocket.Event>

    @Receive
    fun connect(): Flowable<ConnectEvent>

    @Send
    fun sendSubscribe(subscribeEvent: SubscribeEvent): Boolean

    @Receive
    fun observeUpdates(): Flowable<UpdateEvent>
}
