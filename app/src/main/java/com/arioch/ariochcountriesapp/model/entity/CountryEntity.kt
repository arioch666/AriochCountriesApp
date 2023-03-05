package com.arioch.ariochcountriesapp.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
