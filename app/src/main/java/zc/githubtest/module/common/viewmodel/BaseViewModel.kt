package zc.githubtest.module.common.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    fun rxLaunch(job: () -> Disposable?) {
        job()?.let { disposables.add(it) }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}