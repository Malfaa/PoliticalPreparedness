package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch


class ElectionsViewModel(private val repository: ElectionsRepository): ViewModel() {

    init {
        getUpcomingElections()
    }

    //upcoming elections
    val upcomingElections = repository.upcomingElections

    //saved elections
    val savedElections = repository.savedElections


    //Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElections() = viewModelScope.launch {
        try {
            repository.refreshElections()
        }catch (e : Exception) {
            Log.e("ElectionsViewModel", e.toString())
        }
    }
}