package ru.surfstudio.mvi.core.state

import kotlinx.coroutines.flow.Flow

/**
 * State that could be observed and changed.
 */
interface StateHolder<S>: MutableState<S>, ImmutableState<S, Flow<S>>