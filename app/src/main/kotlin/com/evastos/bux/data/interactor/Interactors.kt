package com.evastos.bux.data.interactor

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import io.reactivex.Flowable
import io.reactivex.Single

interface Interactors {

    interface ProductInteractor {
        fun getProductData(request: ProductId): Single<ProductData>
    }

    interface ProductFeedInteractor {
        fun connect(): Flowable<ConnectEvent>

        fun subscribeToChannel(subscribeTo: ProductId, unsubscribeFrom: ProductId? = null): Boolean

        fun observeUpdates(): Flowable<UpdateEvent>
    }
}
