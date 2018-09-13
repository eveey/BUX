package com.evastos.bux.ui.util

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.res.ResourcesCompat

@ColorInt
fun Context.getColorInt(@ColorRes colorRes: Int): Int {
    return ResourcesCompat.getColor(resources, colorRes, theme)
}

fun Context?.isConnectedToNetwork(): Boolean {
    val connectivityManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.activeNetworkInfo?.let {
        return it.isConnected
    }
    return false
}