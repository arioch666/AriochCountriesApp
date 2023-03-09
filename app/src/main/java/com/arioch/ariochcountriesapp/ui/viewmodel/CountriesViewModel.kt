package com.arioch.ariochcountriesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arioch.ariochcountriesapp.model.repo.CountriesRepo
import com.arioch.ariochcountriesapp.network.data.CountryNetworkObj
import com.arioch.ariochcountriesapp.network.data.NetworkResult
import com.arioch.ariochcountriesapp.network.data.NetworkState
import com.arioch.ariochcountriesapp.network.data.toCountryEntityList
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesViewModel(private val countriesRepository: CountriesRepo): ViewModel() {
    val countriesList: LiveData<List<CountryUiObjForList>> = countriesRepository
                                                                .allCountriesUiObjForListFlow
                                                                .asLiveData(
                                                                    viewModelScope.coroutineContext
                                                                )

    val networkStatus: LiveData<NetworkState> = countriesRepository
                                                .networkStateFlow
                                                .asLiveData(viewModelScope.coroutineContext)


    init {
        viewModelScope.launch {
            countriesRepository.networkResultFlow.collectLatest { result ->
                Log.d("arioch viewmodel", "result in viewModel $result")
                if (result::class.java.isAssignableFrom(NetworkResult.NetworkSuccess::class.java)) {
                    val networkSuccessResult = result as NetworkResult.NetworkSuccess<*>
                    val networkResultBody = networkSuccessResult.networkResultBody
                    if(networkResultBody!= null && networkResultBody::class.java.isAssignableFrom(ArrayList::class.java)) {
                        val resultList = networkResultBody as List<*>
                        if (resultList.isNotEmpty() && resultList.first()!!::class.java.isAssignableFrom(CountryNetworkObj::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            val resultListCountryNetworkObj = resultList as List<CountryNetworkObj>
                            val countryEntityList = resultListCountryNetworkObj.toCountryEntityList()
                            withContext(Dispatchers.IO) {
                                countriesRepository.insertCountriesIntoDB(*countryEntityList.toTypedArray())
                            }
                        }
                    }
                }
            }
        }

    }

    fun getCountries(forceNetworkRequest: Boolean, isNetworkConnected: Boolean) {
        viewModelScope.launch {
            Log.d("arioch viewmodel", "forceNetworkRequest: $forceNetworkRequest, isNetworkConnected: $isNetworkConnected")
            countriesRepository.fetchCountries(forceNetworkRequest, isNetworkConnected)
        }
    }
}