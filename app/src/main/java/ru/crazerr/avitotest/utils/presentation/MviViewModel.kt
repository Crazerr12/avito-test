package ru.crazerr.avitotest.utils.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<S : Any, A : Any>(initialState: S) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    abstract fun handleAction(action: A)

    protected fun reduceState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }
}