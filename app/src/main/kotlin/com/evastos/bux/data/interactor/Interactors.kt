package com.evastos.bux.data.interactor

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import io.reactivex.Single

interface Interactors {

    interface ProductDataInteractor : Interactor<ProductId, Single<ProductData>>
//
//    interface LoginInteractor : Interactor<LoginRequest, Single<LoginResponse>>

}
