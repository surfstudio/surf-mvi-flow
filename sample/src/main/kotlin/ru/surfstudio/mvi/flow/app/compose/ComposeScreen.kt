/*
 * Copyright 2022 Surf LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import ru.surfstudio.mvi.vm.compose.renders
import ru.surfstudio.mvi.vm.compose.binds

/**
 * Example composable functions with viewModel
 */
@Composable
fun ComposeScreen(viewModel: ComposeViewModel = viewModel()) {
    viewModel renders { state ->
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Compose function with viewModel")
                Text(text = state.loadStateData)
                Button(onClick = { emit(NetworkEvent.StartLoading) }) {
                    Text("Start compose loading")
                }
            }
        }
    }
}

/**
 * Example composable functions with viewModel, but without state
 */
@Composable
fun ComposeScreenWithoutState(viewModel: ComposeViewModelWithoutState = viewModel()) {
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
