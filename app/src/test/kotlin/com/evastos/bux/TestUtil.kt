package com.evastos.bux

import com.evastos.bux.data.rx.RxSchedulers
import io.reactivex.schedulers.Schedulers

object TestUtil {
    private val trampolineScheduler = Schedulers.trampoline()

    val rxSchedulers = RxSchedulers(
        trampolineScheduler,
        trampolineScheduler,
        trampolineScheduler)
}
