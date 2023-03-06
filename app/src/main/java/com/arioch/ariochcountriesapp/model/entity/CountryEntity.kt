package com.arioch.ariochcountriesapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList

/**
 * Schema for the "countries" table.
 */
@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    val cId: Int = 0,
    val name: String,
    val capital: String,
    val flag: String,
    val code: String,
    val region: String
)

/**
 * Converts the [CountryEntity] to a [CountryUiObjForList].
 */
fun CountryEntity.toCountryUiObjForList() = CountryUiObjForList(cId = cId, name = name, capital = capital, code = code, region = region)

fun List<CountryEntity>.toListOfCountryUiObjForList() = this.map {countryEntity: CountryEntity ->
    countryEntity.toCountryUiObjForList()
}