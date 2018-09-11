package com.evastos.bux.data.interactor

import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable
import io.reactivex.Single

interface Interactors {

    interface ProductInteractor {
        fun getProductDetails(request: ProductId): Single<ProductDetails>
    }

    interface ProductFeedInteractor {
        fun connect(rtfService: RtfService): Flowable<ConnectEvent>

        fun subscribeToChannel(rtfService: RtfService, subscribeTo: ProductId, unsubscribeFrom: ProductId? = null): Boolean

        fun observeUpdates(rtfService: RtfService): Flowable<UpdateEvent>
    }
}
