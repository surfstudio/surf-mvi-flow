package ru.surfstudio.mvibase.logging

interface MviLogger {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
    fun e(tag: String, message: String?, throwable: Throwable)
    fun wtf(tag: String, message: String)
}