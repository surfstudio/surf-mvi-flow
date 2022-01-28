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
package ru.surfstudio.mvi.flow.app.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import ru.surfstudio.mvi.flow.app.R
import ru.surfstudio.mvi.flow.app.request.RequestState
import ru.surfstudio.mvi.flow.lifecycle.MviAndroidView

class SimpleActivity : AppCompatActivity(), MviAndroidView<SimpleState, SimpleEvent> {

    override val viewModel by viewModels<SimpleViewModel> { SimpleViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val counterTv = findViewById<TextView>(R.id.counter_tv)
        val incrementBtn = findViewById<Button>(R.id.increment_btn)
        val decrementBtn = findViewById<Button>(R.id.decrement_btn)

        val clickMeBtn = findViewById<Button>(R.id.click_me_btn)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val loadingBtn = findViewById<Button>(R.id.loading_btn)

        loadingBtn.setOnClickListener { emit(SimpleEvent.StartLoadingClick) }

        incrementBtn.setOnClickListener { emit(SimpleEvent.IncrementClick) }
        decrementBtn.setOnClickListener { emit(SimpleEvent.DecrementClick) }

        clickMeBtn.setOnClickListener { emit(SimpleEvent.SimpleClick) }

        observeState { state: SimpleState ->
            counterTv?.text = state.counter.toString()
            clickMeBtn?.text = state.title

            progressBar.isVisible = state.request == RequestState.Loading
            loadingBtn.isClickable = state.request == RequestState.None
            loadingBtn.text = when(state.request) {
                RequestState.Error -> "Ошибка =("
                RequestState.Loading -> "Загрузка..."
                RequestState.None -> "Старт загрузки"
                RequestState.Success -> "Успех!"
            }
        }
    }
}