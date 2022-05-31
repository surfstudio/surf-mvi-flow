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
package ru.surfstudio.mvi.flow.app.compose.standard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.surfstudio.mvi.flow.app.reused.NetworkCommandEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.vm.compose.bindsCommandEvent
import ru.surfstudio.mvi.vm.compose.renders

private const val itemsSize = 10000

/**
 * Example composable functions with viewModel
 */
@Composable
fun ComposeScreen(viewModel: ComposeViewModel = viewModel()) {
    val listState = rememberLazyListState()
    viewModel bindsCommandEvent { commandEvents ->
        launch {
            when (commandEvents) {
                NetworkCommandEvent.ScrollToBottom -> {
                    listState.scrollToItem(itemsSize - 1)
                }
                else -> {}
            }
        }
    }
    viewModel renders { state ->
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Compose function with state")
                Text(text = state.loadStateData)
                Button(onClick = { emit(NetworkEvent.StartLoading) }) {
                    Text("Start compose loading")
                }
                Button(onClick = { emit(NetworkEvent.DoNothingAndScrollToBottom) }) {
                    Text("Click click! ScrollToBottom")
                }

                LazyColumn(state = listState, modifier = Modifier.height(200.dp)) {
                    items(itemsSize) { index ->
                        Text(text = "First List items : $index")
                    }
                }
            }
        }
    }
}
