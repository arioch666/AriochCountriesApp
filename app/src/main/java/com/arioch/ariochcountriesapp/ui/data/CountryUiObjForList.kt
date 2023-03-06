package com.arioch.ariochcountriesapp.ui.data

/**
 * Represents the data needed do display country info in a list.
 */
data class CountryUiObjForList(
    val cId: Int = 0,
    val name: String,
    val capital: String,
    val code: String,
    val region: String
)
