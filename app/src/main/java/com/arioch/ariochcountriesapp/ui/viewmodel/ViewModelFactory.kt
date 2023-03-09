package com.arioch.ariochcountriesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arioch.ariochcountriesapp.model.repo.CountriesRepo


class ViewModelFactory(private val countriesRepo: CountriesRepo):
    ViewModelProvider.Factory {
    /**
     * Updated this method with new view models if there are more added in the future.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
            val canonicalName = CountriesViewModel::class.java.canonicalName ?:
                                        CountriesViewModel::class.java.simpleName

            if(!hashMap.containsKey(canonicalName)) {
                hashMap[canonicalName] = CountriesViewModel(countriesRepo)
            }
            @Suppress("UNCHECKED_CAST")
            return hashMap[canonicalName] as T
        }
        throw IllegalArgumentException("Unsupported View Model requested.")
    }

    companion object {
        private val hashMap: MutableMap<String, ViewModel> = mutableMapOf()
    }
}