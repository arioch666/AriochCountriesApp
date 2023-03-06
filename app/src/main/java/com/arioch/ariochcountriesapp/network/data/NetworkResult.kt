package com.arioch.ariochcountriesapp.network.data

/**
 * Result of the network request.
 */
interface NetworkResult {

    /**
     * The failure case will propagate the Exception for handling on the UI.
     */
    data class NetworkFailure(val e: Exception): NetworkResult

    /**
     * No Result, Default for the state flow initialization.
     */
    object NetworkSilent: NetworkResult

    /**
     * The Success case will return the network response.
     */
    data class  NetworkSuccess<T>(val networkResultBody: T): NetworkResult
}