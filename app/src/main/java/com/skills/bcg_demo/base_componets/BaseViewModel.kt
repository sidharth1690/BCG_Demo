package com.skills.bcg_demo.base_componets

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    private var subscription: CompositeDisposable? = null
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    val scope = CoroutineScope(coroutineContext)

    init {
        subscription = CompositeDisposable()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        coroutineContext.cancel()
    }

}