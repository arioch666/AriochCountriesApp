package com.arioch.ariochcountriesapp.ui.countries.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arioch.ariochcountriesapp.databinding.CountriesListItemBinding
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList

/**
 * Countries Adapter for the recyclerView that will display the list of countries.
 */
class CountriesRecyclerViewAdapter(private var countriesList: List<CountryUiObjForList>): RecyclerView.Adapter<CountriesViewHolder>() {
    private lateinit var binding: CountriesListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        binding = CountriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountriesViewHolder(binding)
    }

    override fun getItemCount() = countriesList.size


    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countriesList[position])
    }

    /**
     * replaces all values in the existing list with the values from the new list.
     */
    fun replaceList(newList: List<CountryUiObjForList>) {
        countriesList = newList
        notifyItemRangeChanged(0, itemCount)
    }
}