package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.Election

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<Election>()
    val upcomingElections : LiveData<Election>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections
    private val _savedElections = MutableLiveData<Election>()
    val savedElections : LiveData<Election>
        get() = _upcomingElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database


    //TODO: Create functions to navigate to saved or upcoming election voter info


}