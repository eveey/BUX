package com.evastos.bux.data.network.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.evastos.bux.ui.util.isConnectedToNetwork
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class NetworkConnectivityReceiver : BroadcastReceiver() {

    private val networkConnectivitySubject = BehaviorSubject.create<Boolean>()

    val observable = networkConnectivitySubject as Observable<Boolean>

    /* Warning: this method of checking network connectivity will be deprecated.
    Use NetworkCapabilities available since API level 21 (Marshmallow)
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            networkConnectivitySubject.onNext(context.isConnectedToNetwork())
        }
    }
}
