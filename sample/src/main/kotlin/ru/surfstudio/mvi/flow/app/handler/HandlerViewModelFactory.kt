package ru.surfstudio.mvi.flow.app.handler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowStateHolder
import ru.surfstudio.mvi.flow.app.handler.error.ErrorHandlerImpl
import ru.surfstudio.mvi.flow.app.network.IpNetworkCreator
import ru.surfstudio.mvi.flow.lifecycle.ViewModelFlowBinder

class HandlerViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val stateHolder = FlowStateHolder(HandlerState())
        val hub: FlowEventHub<HandlerEvent> = FlowEventHub()
        val middleware = HandlerMiddleware(IpNetworkCreator.repository)
        val reducer = HandlerReducer(ErrorHandlerImpl())

        val viewModel = HandlerViewModel(stateHolder, hub)

        val binder = ViewModelFlowBinder(hub, middleware, stateHolder, reducer)
        binder.bindFlowIn(viewModel.viewModelScope)

        return viewModel as T
    }

}