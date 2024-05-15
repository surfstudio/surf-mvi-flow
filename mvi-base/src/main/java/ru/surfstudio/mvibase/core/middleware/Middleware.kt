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
package ru.surfstudio.mvi.core.middleware

/**
 * Basic object in terms of MVI.
 * Transforms the stream of events upcoming from the EventHub and returns the stream of transformed events.
 *
 * Is used to transform UI events into service layer events.
 * For example for starting network request after pressing UI button.
 *
 * @param InputStream   type of income stream
 * @param OutputStream  type of outcome stream
 */
interface Middleware<InputStream, OutputStream> {

    fun transform(eventStream: InputStream): OutputStream
}