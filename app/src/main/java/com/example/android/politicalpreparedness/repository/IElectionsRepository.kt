package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.flow.Flow

interface IElectionsRepository {

    fun getAllSaved(): LiveData<List<Election>> // ok

    fun getAllUpcoming(): LiveData<List<Election>> // ok  retrieve from cache

    fun getElection(id: Int): VoterInfo // ok

    suspend fun insertAll(elections: List<Election>) // ok  insert into cache

    suspend fun addElectionToDB(election: Election) // ok

    suspend fun removeElectionFromDB(election: Election) // ok

    suspend fun refreshElections() //ok

    suspend fun refreshVoterInfoQuery(address: String, id: Int): VoterInfoResponse

    suspend fun loadVoterInfo(id:Int)
//
//    suspend fun refreshRepresentativeInfoByAddress(address: String): Result<RepresentativeResponse>

}