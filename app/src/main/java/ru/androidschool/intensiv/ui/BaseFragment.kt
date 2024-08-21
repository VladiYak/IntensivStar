package ru.androidschool.intensiv.ui

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}