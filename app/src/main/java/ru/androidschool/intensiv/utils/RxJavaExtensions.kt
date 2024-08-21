package ru.androidschool.intensiv.utils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.applyIoMainSchedulers(
    subscribeOnScheduler: Scheduler = Schedulers.io(),
    observeOnScheduler: Scheduler = AndroidSchedulers.mainThread()
): Single<T> {
    return this.subscribeOn(subscribeOnScheduler)
        .observeOn(observeOnScheduler)
}

fun <T> Observable<T>.applyIoMainSchedulers(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
