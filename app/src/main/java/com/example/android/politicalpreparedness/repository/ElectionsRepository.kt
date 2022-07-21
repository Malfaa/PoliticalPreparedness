package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(
    private val electionDatabase: ElectionDatabase,
    private val api: CivicsApi
) : IElectionsRepository {

    override fun getAllSaved(): LiveData<List<Election>> { //database
        return electionDatabase.electionDao.getSavedElections()
    }

    override fun getAllUpcoming(): LiveData<List<Election>> { //retrieve from cache
        return electionDatabase.electionDao.getUpcomingElections()
    }

    override fun getElection(id: Int): LiveData<Election> { //database
        return electionDatabase.electionDao.getSingle(id)
    }

    override suspend fun insertAll(elections: List<Election>) { // saves on cache
        withContext(Dispatchers.IO){
            electionDatabase.electionDao.addAll(elections)
        }
    }

    override suspend fun addElectionToDB(election: Election) { //database
        withContext(Dispatchers.IO){
            electionDatabase.electionDao.addElection(election)
        }
    }

    override suspend fun removeElectionFromDB(election: Int) { //database
        withContext(Dispatchers.IO){
            electionDatabase.electionDao.deleteElection(election)
        }
    }

    override suspend fun refreshElections() { //remote
        try {
            withContext(Dispatchers.IO) {
                val electionResponse = api.getElections()
                insertAll(electionResponse.elections)
            }

        } catch (e: Exception) {
            Log.e("Refresh Elections Error", e.toString())
        }
    }

//    override suspend fun refreshVoterInfoQuery(
//        address: String,
//        id: Long
//    ): Result<VoterInfoResponse> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun refreshRepresentativeInfoByAddress(address: String): Result<RepresentativeResponse> {
//        TODO("Not yet implemented")
//    }
}