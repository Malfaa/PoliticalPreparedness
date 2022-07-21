package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

interface IElectionsRepository {
    suspend fun refreshElections()

    fun getAllSaved(): LiveData<List<Election>>

    fun getAllUpcoming(): LiveData<List<Election>>

    suspend fun getElection(id: Int): LiveData<Election>

    suspend fun insertAll(elections: List<Election>)

    suspend fun addElectionToDB(election: Election)

    suspend fun removeElectionFromDB(election: Election)

}