package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: RepresentativeRepository): ViewModel() {

    //establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    val representatives = repository.representatives

    val selectedStateIndex = MutableLiveData<Int>()

    //create function to fetch representatives from API from a provided address

    private fun refreshRepresentatives() {
        viewModelScope.launch {
            try {
                _address.value!!.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.value!!.toFormattedString()
                repository.refreshListRep(addressStr)

            } catch (e: Exception) {
                e.printStackTrace()
                //showSnackBarInt.postValue(R.string.no_network_or_address_not_found_msg)
            }
        }
    }

    fun onSearchButtonClick() {
        refreshRepresentatives()
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //create function get address from geo location
    fun refreshByCurrentLocation(address: Address) {
        val stateIndex = _states.value?.indexOf(address.state)

        if (stateIndex != null && stateIndex >= 0) {
            selectedStateIndex.value = stateIndex
            _address.value = address
            refreshRepresentatives()

        } else {
            //showSnackBarInt.value = R.string.current_location_is_not_us_msg
        }
    }

    //create function to get address from individual fields
    private fun getSelectedState(stateIndex: Int) : String {
        return states.value!!.toList()[stateIndex]
    }

}
