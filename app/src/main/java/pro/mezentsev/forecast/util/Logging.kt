package pro.mezentsev.forecast.util

import android.util.Log

fun String.log() {
    Log.d("FORECAST", this)
}

fun String.loge(t: Throwable) {
    Log.e("FORECAST", this, t)
}