package com.arioch.ariochcountriesapp.model.repo

import com.arioch.ariochcountriesapp.model.dao.CountriesDao
import com.arioch.ariochcountriesapp.model.entity.CountryEntity
import com.arioch.ariochcountriesapp.model.entity.toListOfCountryUiObjForList
import com.arioch.ariochcountriesapp.network.CountriesApiService
import com.arioch.ariochcountriesapp.network.data.toCountryEntityList
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

/**
 * Repo for the countries data.
 *
 * Use this in the viewmodel to request countries data.
 *
 * Handles determination of network vs local data fetching.
 */
class CountriesRepo(private val countriesDao: CountriesDao,
                    private val countriesApiService: CountriesApiService) {

    /**
     * Flow of countries data from the local DB.
     */
    private val allCountriesListFlow: Flow<List<CountryEntity>> = countriesDao.getAllCountries()

    /**
     * Flow that emits [CountryUiObjForList] when data from the [allCountriesListFlow] is updated.
     */
    val allCountriesUiObjForListFlow: Flow<List<CountryUiObjForList>> = allCountriesListFlow.map {countryEntityList ->
        countryEntityList.toListOfCountryUiObjForList()
    }.distinctUntilChanged()

    /**
     * Inserts the [CountryEntity] objects into the DB using the [countriesDao].
     *
     * Since room runs all queries on a different thread there is no need to make this a
     * suspend function.
     */
    fun insertCountriesIntoDB(vararg countries: CountryEntity) {
        countriesDao.insertAll(*countries)
    }

    /**
     * Fetches the countries data from the network if local data is unavailable or the refresh is
     * forced.
     */
    suspend fun fetchCountries(forceNetworkFetch: Boolean) {
        // TODO: Add checked values check? or add Cache to retrofit's OkHttpClient.
        if (forceNetworkFetch) {
            requestCountriesFromNetwork()
        }

        countriesDao.getAllCountries()
    }

    /**
     * Triggers a network request for the countries from the API endpoint.
     */
    private suspend fun requestCountriesFromNetwork() {
        // TODO move this try catch to a more central location like a network handler.
        try {
            val countriesFetchResponse = countriesApiService.getCountries()
            if (countriesFetchResponse.isSuccessful) {
                val countriesNetworkNullableList = countriesFetchResponse.body()

                countriesNetworkNullableList?.let { countriesNetworkNonNullList ->
                    val countriesEntityList = countriesNetworkNonNullList.toCountryEntityList()
                    insertCountriesIntoDB(*(countriesEntityList.toTypedArray()))
                }
            } else {
                // TODO handle network error.
            }
        } catch (e: IOException) {
            // TODO move this to a central network handler for centralized error handling.
            // TODO Communicate the error to the UI.
        } catch (e: HttpException) {
            // TODO Communicate the error to the UI.
        }
    }
}