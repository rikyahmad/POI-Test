package com.staygrateful.poi_test.external.util

import android.os.Handler
import android.os.Looper

object HandlerUtil {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun<T> collectAtLeast(delay: Long, data: T, onCall: (T) -> Unit) {
        runnable?.let {
            handler.removeCallbacks(it)
        }
        runnable = Runnable{
            onCall.invoke(data)
        }.also {
            handler.postDelayed(it, delay)
        }
    }
}