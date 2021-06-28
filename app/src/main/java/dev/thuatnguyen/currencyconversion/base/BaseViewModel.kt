package dev.thuatnguyen.currencyconversion.base

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel<State, Event> : ViewModel() {

    private val disposables = CompositeDisposable()

    private val stateMutable: MutableLiveData<State> = MutableLiveData()

    private val eventMutable: MutableLiveData<Event> = MutableLiveData()

    protected abstract val initialState: State

    val state: LiveData<State> = stateMutable

    val event: LiveData<Event> = eventMutable

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    @MainThread
    protected fun setState(state: State) {
        stateMutable.value = state
    }

    protected fun event(event: Event) {
        eventMutable.value = event
    }

    protected fun Disposable.addToDisposables() {
        disposables.add(this)
    }
}

