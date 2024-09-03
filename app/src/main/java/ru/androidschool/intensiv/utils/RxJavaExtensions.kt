package ru.androidschool.intensiv.utils

import android.view.View
import android.widget.ProgressBar
import io.reactivex.Completable
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

fun Completable.applyIoMainSchedulers(): Completable {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.withProgressBar(progressBar: ProgressBar): Single<T> {
    return this.doOnSubscribe {
        progressBar.visibility = View.VISIBLE
    }.doFinally {
        progressBar.visibility = View.GONE
    }
}

fun <T> Observable<T>.withProgressBar(progressBar: ProgressBar): Observable<T> {
    return this.doOnSubscribe {
        progressBar.visibility = View.VISIBLE
    }.doFinally {
        progressBar.visibility = View.GONE
    }
}
