package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class ElectionsViewModel( private val repository: ElectionsRepository): ViewModel() {

    init {
        getUpcomingElections()
    }
    //upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections : LiveData<List<Election>>
        get() = _upcomingElections

    //saved elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections : LiveData<List<Election>>
        get() = _savedElections

    val getSavedElections = repository.getAllSaved()

    fun savedElections(list: List<Election>){
        _savedElections.postValue(list)
    }

    private val _navigate = MutableLiveData<Election?>()
    val navigate: LiveData<Election?>
        get() = _navigate


    //Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElections() = viewModelScope.launch {
        try {
            repository.refreshElections()
            _upcomingElections.value = getSavedElections.value
        }catch (e : Exception) {
            Log.e("ElectionsViewModel", e.toString())
        }
    }

    //functions to navigate to saved or upcoming election voter info
    fun navigateTo(election: Election) {
        _navigate.value = election
    }

    fun navigated() {
        _navigate.value = null
    }

}