package ru.surfstudio.mvi.flow.app.compose.simple

import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.vm.BaseViewModel

class SimpleComposeViewModel : BaseViewModel<SimpleComposeEvent>() {

    override val hub: FlowEventHub<SimpleComposeEvent> = FlowEventHub()
    override val middleware: SimpleComposeMiddleware = SimpleComposeMiddleware()

    init {
        bindFlow()
    }
}