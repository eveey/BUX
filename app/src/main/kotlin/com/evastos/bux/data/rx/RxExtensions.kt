package com.evastos.bux.data.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Single<T>.applySchedulers(rxSchedulers: RxSchedulers): Single<T> {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}

fun Completable.applySchedulers(rxSchedulers: RxSchedulers): Completable {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}

fun <T> Observable<T>.applySchedulers(rxSchedulers: RxSchedulers): Observable<T> {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}