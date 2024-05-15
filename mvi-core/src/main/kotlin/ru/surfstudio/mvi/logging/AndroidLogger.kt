package ru.surfstudio.mvi.logging

import ru.surfstudio.mvibase.logging.MviLogger
import timber.log.Timber

object AndroidLogger: MviLogger {
    override fun v(tag: String, message: String) {
        Timber.v(tag, message)
    }

    override fun d(tag: String, message: String) {
        Timber.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        Timber.i(tag, message)
    }

    override fun w(tag: String, message: String) {
        Timber.w(tag, message)
    }

    override fun e(tag: String, message: String) {
        Timber.e(tag, message)
    }

    override fun e(tag: String, message: String?, throwable: Throwable) {
        Timber.e(tag, message, throwable)
    }

    override fun wtf(tag: String, message: String) {
        Timber.wtf(tag, message)
    }
}