package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.models.Election

interface IElectionsRepository {
    //UPCOMING
    suspend fun refreshElections()


    //ELECTIONS
    suspend fun getElection(id: Int): Election
    suspend fun addElectionToDB(election: Election)
    suspend fun removeElectionFromDB(election: Election)



    //VOTERINFO
    suspend fun refreshVoterInfoQuery(address: String, id: Int)
    suspend fun loadVoterInfo(id:Int)


}