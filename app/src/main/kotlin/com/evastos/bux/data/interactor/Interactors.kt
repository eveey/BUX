package com.evastos.bux.data.interactor

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import io.reactivex.Single

interface Interactors {

    interface ProductInteractor : Interactor<ProductId, Single<ProductData>>

    interface ProductFeedConnectionInteractor : Interactor<Unit, Flowable<WebSocket.Event.OnConnectionOpened<ConnectEvent>>>

    interface ProductFeedSubscriptionInteractor : Interactor<SubscribeEvent, Unit>

    interface ProductFeedUpdateInteractor : Interactor<Unit, UpdateEvent>
}
