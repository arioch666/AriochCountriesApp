package com.arioch.ariochcountriesapp.network.data

import com.arioch.ariochcountriesapp.model.entity.CountryEntity

data class CountryNetworkObj(
    val name: String,
    val capital: String,
    val flag: String,
    val code: String,
    val region: String
)

fun CountryNetworkObj.toCountryEntity() = CountryEntity(name = name, capital = capital, region = region, code = code, flag = flag)

fun List<CountryNetworkObj>.toCountryEntityList() : List<CountryEntity> = this.map {countryNetworkObj: CountryNetworkObj ->
    countryNetworkObj.toCountryEntity()
}
