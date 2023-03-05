package com.arioch.ariochcountriesapp.network

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CountriesApiServiceTest {

    @Test
    fun countriesApiService_getCountries_returnsCountries() = runTest {
        val countries = CountriesApi.countriesApiService.getCountries()
        Assert.assertNotEquals(0 , countries.body()?.size ?: 0)
    }
}