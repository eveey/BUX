package com.evastos.bux.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    val compositeDisposable: CompositeDisposable = CompositeDisposable()) : ViewModel() {

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}