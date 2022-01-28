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
package ru.surfstudio.mvi.mappers.handler

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.flow.lifecycle.MviViewModel

/** More complex [MviViewModel] implementation which reducer is able to handle errors */
abstract class MviErrorHandlerViewModel<S : Any, E : Event> : MviViewModel<S, E>() {

    abstract override val reducer: ErrorHandlerReducer<E, S>
}