package ru.surfstudio.mvi.flow.app.compose.simple

import ru.surfstudio.mvi.vm.BaseViewModel

class SimpleComposeViewModel : BaseViewModel<SimpleComposeEvent>() {

    override val middleware: SimpleComposeMiddleware = SimpleComposeMiddleware()

    init {
        bindFlow()
    }
}