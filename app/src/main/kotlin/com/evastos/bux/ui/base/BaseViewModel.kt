package com.evastos.bux.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    val disposables: CompositeDisposable = CompositeDisposable()) : ViewModel() {

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}