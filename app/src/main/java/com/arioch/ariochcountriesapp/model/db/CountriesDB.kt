package com.arioch.ariochcountriesapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arioch.ariochcountriesapp.model.dao.CountriesDao
import com.arioch.ariochcountriesapp.model.entity.CountryEntity

// Defines the DB for the application.
// exportSchema is set to false. Change to true before release to prod. Do not ship the schema to
// users. That is for rollback/migration purposes only.
@Database(entities = [CountryEntity::class], version = 1, exportSchema = false)
abstract class CountriesDB: RoomDatabase() {

    abstract fun countriedDAO(): CountriesDao

    companion object {
        // Singleton Instance of the DB. Prevents injection of fake DB for testing.
        @Volatile
        private var DB_INSTANCE: CountriesDB? = null

        private val DB_NAME = "countries_db"


        fun getDatabase(context: Context): CountriesDB {
            return DB_INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(context, CountriesDB::class.java, name = DB_NAME).build()
                DB_INSTANCE = instance
                instance
            }
        }
    }
}