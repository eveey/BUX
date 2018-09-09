package com.evastos.bux.ui.base

import android.arch.lifecycle.ViewModel
import com.evastos.bux.data.rx.RxSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    protected val rxSchedulers: RxSchedulers,
    protected val disposables: CompositeDisposable = CompositeDisposable()) : ViewModel() {

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}