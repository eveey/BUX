package com.evastos.bux.data.rx

import io.reactivex.Scheduler

data class RxSchedulers(
    val ioScheduler: Scheduler,
    val mainThreadScheduler: Scheduler,
    val computationScheduler: Scheduler
)
