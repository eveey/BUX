package com.evastos.bux.data.interactor

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import io.reactivex.Single

interface Interactors {

    interface ProductInteractor : Interactor<ProductId, Single<ProductData>>
}
