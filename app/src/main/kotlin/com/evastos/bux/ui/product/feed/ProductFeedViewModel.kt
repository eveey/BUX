package com.evastos.bux.ui.product.feed

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.rtf.connection.ConnectEventType
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel @Inject constructor(
    productFeedInteractor: Interactors.ProductFeedInteractor,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    private var isConnected = false

    init {
        // ugly temporary
        if (isConnected.not()) {
            disposables.add(productFeedInteractor.connect()
                    .applySchedulers(rxSchedulers)
                    .subscribe({
                        Timber.d(it.toString(), "")
                        if (it.eventType == ConnectEventType.CONNECTED) {
                            isConnected = true
                            productFeedInteractor
                                    .subscribeToChannel(ProductId("sb26513"))
                            disposables.addAll(productFeedInteractor.observeUpdates()
                                    .applySchedulers(rxSchedulers)
                                    .subscribe({ updateEvent ->
                                        Timber.d(updateEvent.toString(), "")
                                    }, { throwable ->
                                        Timber.d(throwable.toString(), "")
                                    }))
                        }
                    }, {
                        Timber.d(it.toString(), "")
                    }))
        } else {

            productFeedInteractor
                    .subscribeToChannel(ProductId("sb26513"))
            disposables.addAll(productFeedInteractor.observeUpdates()
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.d(updateEvent.toString(), "")
                    }, { throwable ->
                        Timber.d(throwable.toString(), "")
                    }))
        }

    }
}
