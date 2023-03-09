package com.arioch.ariochcountriesapp.model.repo

import android.util.Log
import com.arioch.ariochcountriesapp.model.dao.CountriesDao
import com.arioch.ariochcountriesapp.model.entity.CountryEntity
import com.arioch.ariochcountriesapp.model.entity.toListOfCountryUiObjForList
import com.arioch.ariochcountriesapp.network.NetworkHandler
import com.arioch.ariochcountriesapp.network.data.NetworkResult
import com.arioch.ariochcountriesapp.network.data.NetworkState
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Repo for the countries data.
 *
 * Use this in the viewmodel to request countries data.
 *
 * Handles determination of network vs local data fetching.
 */
class CountriesRepo(private val countriesDao: CountriesDao) {
    /**
     * Flow of countries data from the local DB.
     */
    private val allCountriesListFlow: Flow<List<CountryEntity>> = countriesDao.getAllCountries().onStart {
        emit(listOf())
    }

    private val networkHandler = NetworkHandler.getNetworkHandler()
    val networkStateFlow: Flow<NetworkState> = networkHandler.networkStateFlow
    val networkResultFlow: Flow<NetworkResult> = networkHandler
        .networkResultFlow

    /**
     * Flow that emits [CountryUiObjForList] when data from the [allCountriesListFlow] is updated.
     */
    val allCountriesUiObjForListFlow: Flow<List<CountryUiObjForList>> = allCountriesListFlow.map {countryEntityList ->
        countryEntityList.toListOfCountryUiObjForList()
    }

    /**
     * Inserts the [CountryEntity] objects into the DB using the [countriesDao].
     *
     * Since room runs all queries on a different thread there is no need to make this a
     * suspend function.
     */
    suspend fun insertCountriesIntoDB(vararg countries: CountryEntity) {
        countriesDao.insertAll(*countries)
    }

    /**
     * Fetches the countries data from the network if local data is unavailable or the refresh is
     * forced.
     */
    suspend fun fetchCountries(forceNetworkFetch: Boolean, isNetworkConnected: Boolean) {
        if (forceNetworkFetch) {
            requestCountriesFromNetwork(isNetworkConnected)
        }

        countriesDao.getAllCountries()
    }

    /**
     * Triggers a network request for the countries from the API endpoint.
     */
    private suspend fun requestCountriesFromNetwork(isNetworkConnected: Boolean) {
        /**
         * The Network handler will take care of errors and pass those along through the network
         * state or network result flows.
         */
        networkHandler.makeCountriesRequest(isNetworkConnected)
    }
}