package com.arioch.ariochcountriesapp.network

import com.arioch.ariochcountriesapp.network.data.CountryNetworkObj
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit Service that will provide access to the countries JSON data.
 */
interface CountriesApiService {
    @GET(API.COUNTRIES)
    suspend fun getCountries(): Response<List<CountryNetworkObj>>
}

object CountriesApi {
    internal val countriesApiService: CountriesApiService by lazy {
        RetrofitProvider.getRetrofitInstance().create(CountriesApiService::class.java)
    }
}