package ru.surfstudio.mvi.flow.app.compose.simple

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.surfstudio.mvi.vm.compose.binds

/**
 * Example composable functions with viewModel, but without state
 */
@Composable
fun SimpleComposeScreen(viewModel: SimpleComposeViewModel = viewModel()) {
    viewModel binds {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Compose function with viewModel, but without state")
                Button(onClick = { emit(SimpleComposeEvent.SimpleClickEventEvent) }) {
                    Text("Click click! See log")
                }
            }
        }
    }
}