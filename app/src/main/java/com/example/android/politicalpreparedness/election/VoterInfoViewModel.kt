package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: ElectionsRepository, private val id: Int, private val division: Division) : ViewModel() { // Ter método pra salvar quando apertar botão, colocar info no fragmento

    //Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo : LiveData<VoterInfo>
        get()= _voterInfo

    private val getVoterInfo = repository.getElection(id)

    //TODO: Add var and methods to populate voter info



    //Add var and methods to support loading URLs



    //Add var and methods to save and remove elections to local database
    fun followButton(){
        viewModelScope.launch {
            _
            if (getVoterInfo.value?.saved == true){
                repository.removeElectionFromDB(id)
            }else{
                repository.addElectionToDB(it)
            }
        }
    }

//TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

/**
 * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
 */

}