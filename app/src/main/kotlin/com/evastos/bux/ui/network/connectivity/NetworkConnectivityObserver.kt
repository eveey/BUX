package com.evastos.bux.ui.network.connectivity

/**
 * Observes the network connectivity change events.
 */
interface NetworkConnectivityObserver {

    fun onNetworkConnectivityAcquired()

    fun onNetworkConnectivityLost()
}
