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
package ru.surfstudio.mvi.flow

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import ru.surfstudio.mvi.core.event.Event
import kotlin.reflect.KClass

/**
 * List with all transformations of the screen events.
 *
 * @param eventStream source stream with screen events
 */
open class EventTransformerList<E : Event>(
    val eventStream: Flow<E>
) : MutableList<Flow<E>> by ArrayList() {

    /**
     * Unary operator overload to add Flows directly to the list
     */
    operator fun Flow<E>.unaryPlus() {
        add(this)
    }

    /**
     * Add several transformations to the list
     */
    fun addAll(vararg transformations: Flow<E>) {
        addAll(transformations.toList())
    }


    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param action функция реакции.
     */
    @OptIn(FlowPreview::class)
    inline infix fun <reified T : Event> Flow<T>.react(
        noinline action: (T) -> Unit
    ): Flow<T> {
        return flatMapMerge {
            action(it)
            emptyFlow()
        }
    }

    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param action функция реакции.
     */
    inline fun <reified T : Event> react(
        noinline action: (T) -> Unit
    ): Flow<T> {
        return eventStream.filterIsInstance<T>().react(action)
    }

    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param action функция реакции.
     */
    inline infix fun <reified T> KClass<T>.react(
        noinline action: (T) -> Unit
    ): Flow<E> where T : E {
        return eventStream.filterIsInstance<T>().react(action)
    }

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> Flow<T>.eventToEvent(
        noinline mapper: suspend (T) -> E
    ): Flow<E> {
        return map(mapper)
    }

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> eventToEvent(
        noinline mapper: suspend (T) -> E
    ): Flow<E> {
        return eventStream.filterIsInstance<T>() eventToEvent mapper
    }

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.eventToEvent(
        noinline mapper: suspend (T) -> E
    ): Flow<E> {
        return eventStream.filterIsInstance<T>().eventToEvent(mapper)
    }

    /**
     * Maps events of type [T] to [Flow]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    @OptIn(FlowPreview::class)
    infix fun <T : Event> Flow<T>.eventToStream(
        mapper: (T) -> Flow<E>
    ): Flow<E> {
        return this.flatMapMerge { mapper(it) }
    }

    /**
     * Maps events of type [T] to [Flow]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> eventToStream(
        noinline mapper: (T) -> Flow<E>
    ): Flow<E> {
        return eventStream.filterIsInstance<T>().eventToStream(mapper)
    }

    /**
     * Maps events of type [T] to [Flow]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.eventToStream(
        noinline mapper: (T) -> Flow<E>
    ): Flow<E> {
        return eventStream.filterIsInstance<T>().eventToStream(mapper)
    }

    /**
     * Maps [Flow]<[T]> to [Flow]<[E]>.
     *
     * Can be used when the [eventToStream] is not enough, and we should modify source [Flow].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     * @param mapper mapper function.
     */
    infix fun <T : Event> Flow<T>.streamToStream(
        mapper: (Flow<T>) -> Flow<E>
    ): Flow<E> {
        return mapper(this)
    }

    /**
     * Maps [Flow]<[T]> to [Flow]<[E]>.
     *
     * Can be used when the [eventToStream] is not enough, and we should modify source [Flow].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> streamToStream(
        noinline mapper: (Flow<T>) -> Flow<E>
    ): Flow<E> {
        return eventStream.filterIsInstance<T>().streamToStream(mapper)
    }

    /**
     * Maps [Flow]<[T]> to [Flow]<[E]>.
     *
     * Can be used when the [eventToStream] is not enough, and we should modify source [Flow].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.streamToStream(
        noinline mapper: (Flow<T>) -> Flow<E>
    ): Flow<E> {
        return eventStream.filterIsInstance<T>().streamToStream(mapper)
    }

    /**
     * Filters events by a given [filterCondition].
     */
    inline infix fun <reified T : Event> KClass<T>.filter(noinline filterCondition: (T) -> Boolean): Flow<T> {
        return eventStream.filterIsInstance<T>().filter(filterCondition)
    }

    /**
     * Roadmap
     * 1. Decompose events filtered by type, to process them in another middleware.
     */
}