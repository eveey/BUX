package com.evastos.bux.data.repository

import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable
import io.reactivex.Single

interface Repositories {

    interface ProductDetailsRepository {
        fun getProductDetails(productIdentifier: String): Single<ProductDetails>
    }

    interface ProductFeedRepository {
        fun connect(rtfService: RtfService): Flowable<ConnectEvent>

        fun subscribeToChannel(rtfService: RtfService, subscribeTo: String, unsubscribeFrom: String? = null): Boolean

        fun observeUpdates(rtfService: RtfService): Flowable<UpdateEvent>
    }
}
