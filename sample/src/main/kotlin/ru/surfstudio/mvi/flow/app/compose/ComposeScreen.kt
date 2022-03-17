package ru.surfstudio.mvi.flow.app.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.lifecycle.renders

@Composable
fun ComposeScreen(viewModel: ComposeViewModel = viewModel()) {
    viewModel renders { state ->
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = state.loadStateData)
                Button(onClick = { emit(NetworkEvent.StartLoading) }) {
                    Text("Start compose loading")
                }
            }
        }
    }
}

