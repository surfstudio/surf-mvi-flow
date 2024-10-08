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
package ru.surfstudio.mvi.flow.app.handler

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import ru.surfstudio.mvi.flow.app.R
import ru.surfstudio.mvi.flow.app.network.IpNetworkCreator
import ru.surfstudio.mvi.flow.app.reused.NetworkCommandEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkEvent
import ru.surfstudio.mvi.flow.app.reused.NetworkState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerAndroidView
import ru.surfstudio.mvi.vm.android.bindsCommandEvent

class HandlerActivity : AppCompatActivity(),
    MviErrorHandlerAndroidView<NetworkState, NetworkEvent> {

    override val viewModel by viewModels<HandlerViewModel> {
        // could be hiltViewModel for a real app
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HandlerViewModel(
                    loadOnStart = true,
                    repository = IpNetworkCreator.repository,
                    dispatcher = Dispatchers.IO
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)
        val contentTv = findViewById<TextView>(R.id.handler_content_tv)

        viewModel bindsCommandEvent { commandEvents ->
            when (commandEvents) {
                is NetworkCommandEvent.ShowSnackSuccessLoading -> {
                    Toast.makeText(
                        this@HandlerActivity,
                        "Loading is completed",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                else -> {}
            }
        }

        observeState { state: NetworkState ->
            contentTv.text = state.loadStateData
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        emit(NetworkEvent.OnBackPressed)
    }
}