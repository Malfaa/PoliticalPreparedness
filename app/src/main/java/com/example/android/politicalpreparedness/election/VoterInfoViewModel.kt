package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: ElectionsRepository): ViewModel(){

    companion object {
        private const val DEFAULT_STATE = "la"
    }

    private val _selectedElection = MutableLiveData<Election>()
    val selectedElection : LiveData<Election>
        get() = _selectedElection

    val voterInfo = repository.voterInfo

    private val _isElectionSaved = MutableLiveData<Boolean?>()
    val isElectionSaved : LiveData<Boolean?>
        get() = _isElectionSaved

    private val _ballot = MutableLiveData<Boolean>(false)
    val ballot : LiveData<Boolean>
        get()=_ballot

    private val _location = MutableLiveData<Boolean>(false)
    val location : LiveData<Boolean>
        get()=_location

    fun data(data: Election) {
        _selectedElection.value = data
        refreshIsElectionSaved(data)
        refreshVoterInfo(data)
    }

    private fun refreshIsElectionSaved(data: Election) {
        viewModelScope.launch {
            try {
                val savedElection = repository.getElection(data.id)
                _isElectionSaved.postValue(savedElection != null)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun refreshVoterInfo(data: Election) {
        viewModelScope.launch {
            try {
                val state = if(data.division.state.isNotEmpty()) data.division.state else DEFAULT_STATE
                val address = "${state},${data.division.country}"

                repository.refreshVoterInfoQuery(address, data.id)
                repository.loadVoterInfo(data.id)

            } catch (e: Exception) {
                e.printStackTrace()
                //showSnackBarInt.postValue(R.string.fail_no_network_msg)
                repository.loadVoterInfo(data.id)
            }
        }
    }

    fun onFollowButtonClick() {
        viewModelScope.launch {
            _selectedElection.value?.let {
                if(isElectionSaved.value == true) {
                    repository.removeElectionFromDB(it)
                } else {
                    repository.addElectionToDB(it)
                }
                refreshIsElectionSaved(it)
            }
        }
    }

    fun stateBallotChange(){
        _ballot.value = true
    }
    fun stateBallotReturn(){
        _ballot.value = false
    }

    fun stateLocationChange(){
        _location.value = true
    }
    fun stateLocationReturn(){
        _location.value = false
    }
}

