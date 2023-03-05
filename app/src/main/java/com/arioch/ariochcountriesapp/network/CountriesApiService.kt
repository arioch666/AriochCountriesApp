package com.arioch.ariochcountriesapp.network

import com.arioch.ariochcountriesapp.network.data.CountryNetworkObj
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


// Base URI for the Retrofit service used in the application.
// TODO: Move this to a build variable to allow for environment specific URLs.
private const val BASE_URL =
    "https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/"

// Retrofit Object
private val retrofit = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

/**
 * Retrofit Service that will provide access to the countries JSON data.
 */
interface CountriesApiService {
    @GET(API.COUNTRIES)
    suspend fun getCountries(): Response<List<CountryNetworkObj>>
}

object CountriesApi {
    val countriesApiService: CountriesApiService by lazy {
        retrofit.create(CountriesApiService::class.java)
    }
}