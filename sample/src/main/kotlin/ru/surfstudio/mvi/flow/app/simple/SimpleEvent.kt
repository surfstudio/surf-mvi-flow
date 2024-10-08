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

import androidx.lifecycle.Lifecycle
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.event.MviLifecycleEvent
import ru.surfstudio.mvi.flow.app.simple.request.RequestState

internal sealed class SimpleEvent : Event {

    data class LifecycleEvent(override var event: Lifecycle.Event) : SimpleEvent(),
        MviLifecycleEvent

    object SimpleClick : SimpleEvent()
    object IncrementClick : SimpleEvent()
    object DecrementClick : SimpleEvent()

    object StartLoadingClick : SimpleEvent()

    data class RequestEvent(val request: RequestState) : SimpleEvent()

    data class TitleUpdate(val title: String) : SimpleEvent()
}