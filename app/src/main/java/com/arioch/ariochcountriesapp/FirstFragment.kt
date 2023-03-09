package com.arioch.ariochcountriesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arioch.ariochcountriesapp.databinding.FragmentFirstBinding
import com.arioch.ariochcountriesapp.network.NetworkConnectivityHelper
import com.arioch.ariochcountriesapp.network.data.NetworkState
import com.arioch.ariochcountriesapp.ui.countries.recycler.CountriesRecyclerViewAdapter
import com.arioch.ariochcountriesapp.ui.data.CountryUiObjForList
import com.arioch.ariochcountriesapp.ui.viewmodel.CountriesViewModel
import com.arioch.ariochcountriesapp.ui.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var viewModel: CountriesViewModel? = null
    private var adapter: CountriesRecyclerViewAdapter? = null
    private var networkConnectivityHelper: NetworkConnectivityHelper? = null
    private var countriesList: List<CountryUiObjForList> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        initializeNetworkConnectivityHelper()

        val application = inflater
            .context
            .applicationContext as AndroidCountriesApplication

        viewModel = ViewModelFactory(application.countriesRepo)
            .create(CountriesViewModel::class.java)

        addCountriesListObserver()
        addNetworkStateObserver()
        addRetryClickListener()

        if (adapter == null) {
            adapter = CountriesRecyclerViewAdapter(countriesList = countriesList)
        }
        binding.recyclerViewCountries.adapter = adapter

        viewModel?.getCountries(forceNetworkRequest = false, isNetworkConnected = networkConnectivityHelper?.isConnected() ?: false)

        return binding.root
    }

    private fun addRetryClickListener() {
        binding.buttonRetry.setOnClickListener {
            viewModel?.
            getCountries(forceNetworkRequest = true,
                networkConnectivityHelper?.isConnected()?: false)
        }
    }

    private fun initializeNetworkConnectivityHelper() {
        if (networkConnectivityHelper == null) {
            networkConnectivityHelper = context?.let { nonNullContext ->
                NetworkConnectivityHelper(nonNullContext)
            }
        }
    }

    private fun addNetworkStateObserver() {
        /**
         * TODO fix the network state. It has not been completely setup.
         */
        viewModel?.networkStatus?.observe(this.viewLifecycleOwner) { networkState ->
            Log.d("ariochFragment", "NetworkState: $networkState")
            when (networkState) {
                is NetworkState.NotConnected -> {
                    Snackbar.make(
                        binding.coordinatorLayoutFragmentFirst,
                        getString(R.string.network_disconnected),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                NetworkState.Idle -> binding.linearProgressIndicator.visibility = View.INVISIBLE
                NetworkState.Loading -> binding.linearProgressIndicator.visibility = View.VISIBLE
                else -> {}
            }
        }
    }

    private fun addCountriesListObserver() {
        viewModel?.countriesList?.observe(this.viewLifecycleOwner) { countryUiObjList ->
            if (countryUiObjList.isEmpty()) {
                Log.d("arioch", "Empty List of countries")
                binding.recyclerViewCountries.visibility = View.INVISIBLE
                binding.textViewErrorOrEmpty.text = getString(R.string.countries_list_empty)
                binding.frameLayoutErrorOrEmpty.visibility = View.VISIBLE
            } else {
                Log.d("arioch", "List of countries: ${countryUiObjList.size}")
                countriesList = countryUiObjList
                (adapter as CountriesRecyclerViewAdapter).replaceList(countriesList)
                binding.recyclerViewCountries.visibility = View.VISIBLE
                binding.frameLayoutErrorOrEmpty.visibility = View.GONE
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}