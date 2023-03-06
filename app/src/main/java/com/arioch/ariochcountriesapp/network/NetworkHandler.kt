package com.arioch.ariochcountriesapp.network

import android.content.Context
import com.arioch.ariochcountriesapp.network.data.NetworkResult
import com.arioch.ariochcountriesapp.network.data.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    val networkStateFlow: Flow<NetworkState> = mutableNetworkStateFlow.distinctUntilChanged { old, new -> old == new }
    val networkResultFlow: Flow<NetworkResult> = mutableNetworkResultFlow.distinctUntilChanged { old, new -> old == new }

    /**
     * Makes the network requests passed in.
     *
     * Central place for handling error cases
     */
    private suspend inline fun <T> makeNetworkRequest(context: Context, request: ()->Response<T>) {
        try {
            if (NetworkConnectivityHelper.isConnected(context).not()) {
                mutableNetworkStateFlow.emit(NetworkState.NotConnected)
                return
            }
            mutableNetworkStateFlow.emit(NetworkState.Loading)
            val response = request()
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
     * @param context is used to determine if the network is available.
     */
    suspend fun makeCountriesRequest(context: Context) {
        makeNetworkRequest(context = context) {
           CountriesApi.countriesApiService.getCountries()
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