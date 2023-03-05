package com.arioch.ariochcountriesapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.arioch.ariochcountriesapp.model.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Provides Read and Write operations on the "countries" table.
 */
@Dao
interface CountriesDao {
    /**
     * Gets all the values in the "countries" table.
     */
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<CountryEntity>>

    /**
     * Inserts all the countries passed in into the "countries" table.
     */
    @Insert
    fun insertAll(vararg countries: CountryEntity)
}