package ru.surfstudio.mvi.flow.app.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowStateHolder
import ru.surfstudio.mvi.flow.lifecycle.ViewModelFlowBinder

class SimpleViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stateHolder = FlowStateHolder(SimpleState())
        val hub = FlowEventHub<SimpleEvent>()
        val middleware = SimpleMiddleware(stateHolder)
        val reducer = SimpleReducer()

        val binder = ViewModelFlowBinder(hub, middleware, stateHolder, reducer)
        val simpleViewModel = SimpleViewModel(stateHolder, hub)

        binder.bindFlowIn(simpleViewModel.viewModelScope)

        return simpleViewModel as T
    }
}