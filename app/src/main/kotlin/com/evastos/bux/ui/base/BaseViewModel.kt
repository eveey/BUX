package com.evastos.bux.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel
@Inject constructor(
    protected val rxSchedulers: RxSchedulers
) : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    val networkConnectivityLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun observeNetworkConnectivity(networkConnectivityObservable: Observable<Boolean>) {
        disposables.add(networkConnectivityObservable
                .distinctUntilChanged()
                .applySchedulers(rxSchedulers)
                .subscribe({ isConnected ->
                    networkConnectivityLiveData.postValue(isConnected)
                }, {
                    Timber.e(it)
                    // ignored
                }))
    }
}