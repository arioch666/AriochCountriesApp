package com.arioch.ariochcountriesapp.network.data

/**
 * Used to communicate the state of the network.
 */
interface NetworkState{
    object Idle: NetworkState
    object Loading: NetworkState
    object NotConnected: NetworkState
}
