package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.RepresentativeFragment.Companion.REP_LIST_KEY
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: RepresentativeRepository, app: Application, savedStateHandle: SavedStateHandle): ViewModel() {

    //establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    var representatives = repository._representatives

    val selectedStateIndex = MutableLiveData<Int>()

    private val ssh = savedStateHandle

    init {
        _address.value = Address("", "", "", "New York", "")
        _states.value = app.resources.getStringArray(R.array.states).toList()
        //ssh.set(REP_LIST_KEY, repository._representatives.value )
    }

    //Tried to use SavedStateHandle, no progress
    fun getRepresentativesSaved(){
        representatives = ssh.getLiveData<List<Representative>>(REP_LIST_KEY)
    }

    //create function to fetch representatives from API from a provided address

    private fun refreshRepresentatives() {
        viewModelScope.launch {
            try {
                address.value!!.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.value!!.toFormattedString()
                repository.refreshListRep(addressStr)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun savingStance(address: Address) {
        val stateIndex = _states.value?.indexOf(address.state)

        if (stateIndex != null && stateIndex >= 0) {
            selectedStateIndex.value = stateIndex
            _address.value = address

        }
    }

    //create function get address from geo location
    fun refreshByCurrentLocation(address: Address) {
        val stateIndex = _states.value?.indexOf(address.state)

        if (stateIndex != null && stateIndex >= 0) {
            selectedStateIndex.value = stateIndex
            _address.value = address
            refreshRepresentatives()

        }
    }

    fun onSearchButtonClick() {
        refreshRepresentatives()
    }

    //create function to get address from individual fields
    private fun getSelectedState(stateIndex: Int): String {
        return states.value!!.toList()[stateIndex]
    }

}

// TODO: Criei novo db, dao, nova costante, criar novo Representative pra usar, mudei Repositorio, viewmodelfactory, viewmodel, fragment
// TODO: Descobrir como salvar a lista, por meio do db ou saved instance
// TODO: talvez colocar Representatives como parcelable? será que ajuda na lista?
/**
 *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

val (offices, officials) = getRepresentativesDeferred.await()
_representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

Note: getRepresentatives in the above code represents the method used to fetch data from the API
Note: _representatives in the above code represents the established mutable live data housing representatives

 */