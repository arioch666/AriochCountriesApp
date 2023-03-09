package com.arioch.ariochcountriesapp.network

import com.arioch.ariochcountriesapp.network.data.NetworkResult
import com.arioch.ariochcountriesapp.network.data.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Network requests for data pass through here.
 *
 * Central location to handle errors and emit data.
 */
class NetworkHandler private constructor() {
    private val mutableNetworkStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    private val mutableNetworkResultFlow = MutableStateFlow<NetworkResult>(NetworkResult.NetworkSilent)
    val networkStateFlow: Flow<NetworkState> = mutableNetworkStateFlow
    val networkResultFlow: Flow<NetworkResult> = mutableNetworkResultFlow

    /**
     * Makes the network requests passed in.
     *
     * Central place for handling error cases
     */
    private suspend inline fun <T> makeNetworkRequest(request: ()->Response<T>) {
        try {
            val response = request()
            mutableNetworkStateFlow.emit(NetworkState.Loading)

            if (response.isSuccessful) {
                mutableNetworkResultFlow.emit(NetworkResult.NetworkSuccess(response.body()))
                mutableNetworkStateFlow.emit(NetworkState.Idle)
            } else {
                mutableNetworkResultFlow.emit(NetworkResult.NetworkFailure(HttpException(response)))
                mutableNetworkStateFlow.emit(NetworkState.Idle)
            }
        } catch (e: IOException) {
            // Handling IOException and HttpException separately in case we want to log something
            // here.
            mutableNetworkResultFlow.emit(NetworkResult.NetworkFailure(e))
            mutableNetworkStateFlow.emit(NetworkState.Idle)
        } catch (e: HttpException) {
            mutableNetworkResultFlow.emit(NetworkResult.NetworkFailure(e))
            mutableNetworkStateFlow.emit(NetworkState.Idle)
        }
    }

    /**
     * Makes the network request for countries data.
     */
    suspend fun makeCountriesRequest(isNetworkConnected: Boolean) {
        if (isNetworkConnected.not()) {
            mutableNetworkStateFlow.emit(NetworkState.NotConnected)
        } else {
            makeNetworkRequest {
                CountriesApi.countriesApiService.getCountries()
            }
        }
    }

    companion object {
        // Making this a singleton since all network requests can go thought a single object.
        // Ideally use dependency injection with qualifiers for fakes.
        private var INSTANCE: NetworkHandler? = null
        fun getNetworkHandler(): NetworkHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = NetworkHandler()
                INSTANCE = instance
                instance
            }
        }
    }
}