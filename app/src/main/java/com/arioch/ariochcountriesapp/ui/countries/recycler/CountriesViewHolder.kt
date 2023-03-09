package com.arioch.ariochcountriesapp.ui.countries.recycler

import androidx.recyclerview.widget.RecyclerView
import com.arioch.ariochcountriesapp.databinding.CountriesListItemBinding
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList

/**
 * [ViewHolder] for the recyclerView Adapter that displays countries.
 */
class CountriesViewHolder(private val binding: CountriesListItemBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(country: CountryUiObjForList) {
        //TODO add databinding
        binding.textViewCapital.text = country.capital
        binding.textViewCode.text = country.code
        binding.textViewName.text = country.name
        binding.textViewRegion.text = country.region
    }
}