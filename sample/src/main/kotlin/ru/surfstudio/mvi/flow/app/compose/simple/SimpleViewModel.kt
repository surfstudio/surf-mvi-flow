package ru.surfstudio.mvi.flow.app.compose.simple

import ru.surfstudio.mvi.vm.MviViewModel

class SimpleComposeViewModel : MviViewModel<SimpleComposeEvent>() {

    override val middleware: SimpleComposeMiddleware = SimpleComposeMiddleware()

    init {
        bindFlow()
    }
}