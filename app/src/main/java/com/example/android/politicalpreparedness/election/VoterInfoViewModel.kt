package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

// TODO: ALTERAR CÓDIGO
class VoterInfoViewModel(private val repository: ElectionsRepository): ViewModel()/*BaseViewModel(app) */{

    companion object {
        private const val DEFAULT_STATE = "ny"
    }

    private val _selectedElection = MutableLiveData<Election>()
    val selectedElection : LiveData<Election>
        get() = _selectedElection

    val voterInfo = repository.voterInfo

    private val _isElectionSaved = MutableLiveData<Boolean?>()
    val isElectionSaved : LiveData<Boolean?>
        get() = _isElectionSaved

//    private val mockData = true
//    val mockVoterInfo = MutableLiveData<VoterInfo>()
//
//    init {
//        if(mockData) {
//            val data = VoterInfo(
//                2000,
//                "State XYZ",
//                "",
//                "")
//            mockVoterInfo.postValue(data)
//        }
//
//        _isElectionSaved.value = null
//    }

    fun refresh(data: Election) {
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
}

