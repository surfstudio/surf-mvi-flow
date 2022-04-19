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
package ru.surfstudio.mvi.flow.app.compose.simple

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.vm.compose.binds
import ru.surfstudio.mvi.vm.compose.bindsCommandEvent
import ru.surfstudio.mvi.flow.app.compose.simple.SimpleComposeEvent.CommandEvents.*

/**
 * Example composable functions with viewModel, but without state
 */
@Composable
fun SimpleComposeScreen(viewModel: SimpleComposeViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.bindsCommandEvent { singleLiveEvent ->
        launch {
            when (singleLiveEvent) {
                is ShowMessage -> {
                    snackbarHostState.showSnackbar("Good job")
                }
            }
        }
    }
    SnackbarHost(hostState = snackbarHostState)

    viewModel binds {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Compose function with viewModel, but without state")
                Button(onClick = { emit(SimpleComposeEvent.SimpleClickEventEvent) }) {
                    Text("Click click! Show snack!")
                }

            }
        }
    }
}