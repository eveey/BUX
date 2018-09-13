package com.evastos.bux.ui.base.network

interface NetworkConnectivityObserver {

    fun onNetworkConnectivityAcquired()

    fun onNetworkConnectivityLost()
}