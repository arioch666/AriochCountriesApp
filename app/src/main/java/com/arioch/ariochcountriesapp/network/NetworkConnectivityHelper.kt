package com.arioch.ariochcountriesapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Assists with checking if the device is connected before making network calls.
 */
object NetworkConnectivityHelper {
    /**
     * Checks for the network capabilities and returns true if the network is available
     * false otherwise.
     */
    fun isConnected(context: Context): Boolean {
        try {
            val connectivityManager = context.
                                        applicationContext
                                            .getSystemService(Context.CONNECTIVITY_SERVICE)
                                                as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                                        ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } catch(e: Exception) {
            return false
        }

    }
}