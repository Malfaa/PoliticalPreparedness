package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.flow.Flow

interface IElectionsRepository {

    fun getAllSaved(): LiveData<List<Election>> // ok

    fun getAllUpcoming(): LiveData<List<Election>> // ok  retrieve from cache

    fun getElection(id: Int): LiveData<Election> // ok

    suspend fun insertAll(elections: List<Election>) // ok  insert into cache

    suspend fun addElectionToDB(election: Election) // ok

    suspend fun removeElectionFromDB(election: Int) // ok

    suspend fun refreshElections() //ok

//    suspend fun refreshVoterInfoQuery(address: String, id: Long): Result<VoterInfoResponse>
//
//    suspend fun refreshRepresentativeInfoByAddress(address: String): Result<RepresentativeResponse>

}