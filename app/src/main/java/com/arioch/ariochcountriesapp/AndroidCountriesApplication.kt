package com.arioch.ariochcountriesapp

import android.app.Application
import com.arioch.ariochcountriesapp.model.db.CountriesDB
import com.arioch.ariochcountriesapp.model.repo.CountriesRepo
import com.arioch.ariochcountriesapp.network.RetrofitProvider
import kotlinx.coroutines.CoroutineScope

class AndroidCountriesApplication: Application() {
    private val countriesDB by lazy {
        CountriesDB.getDatabase(this)
    }

    val countriesRepo by lazy {
        CountriesRepo(countriesDB.countriedDAO())
    }
}
