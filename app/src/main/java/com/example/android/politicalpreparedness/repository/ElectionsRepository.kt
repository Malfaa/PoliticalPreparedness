package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.database.voterinfo.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(
    private val electionDatabase: ElectionDatabase,
    private val voterInfoDatabase: VoterInfoDatabase,
    private val api: CivicsApi
) : IElectionsRepository {

    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

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
        withContext(Dispatchers.IO) {
            electionDatabase.electionDao.addAll(elections)
        }
    }

    override suspend fun addElectionToDB(election: Election) { //database
        withContext(Dispatchers.IO) {
            electionDatabase.electionDao.addElection(election)
        }
    }

    override suspend fun removeElectionFromDB(election: Election) { //database
        withContext(Dispatchers.IO) {
            electionDatabase.electionDao.deleteElection(election)
        }
    }

    override suspend fun refreshElections() { //remote Elections Repo
        try {
            withContext(Dispatchers.IO) {
                val electionResponse = api.retrofitService.getElections()
                insertAll(electionResponse.elections)
            }

        } catch (e: Exception) {
            Log.e("Refresh Elections Error", e.toString())
        }
    }

    private fun convertResponseToVoterModel(response: VoterInfoResponse, id: Int) : VoterInfo? {
        var voterInfo: VoterInfo? = null
        val state = response.state
        if (state?.isNotEmpty() == true) {
            val votingLocatinUrl: String =
                state[0].electionAdministrationBody.votingLocationFinderUrl?.run {this}.toString()

            val ballotInfoUrl: String =
                state[0].electionAdministrationBody.ballotInfoUrl?.run {this}.toString()

            voterInfo = VoterInfo(
                id,
                state[0].name,
                votingLocatinUrl,
                ballotInfoUrl
            )
        }
        return voterInfo
    }

    override suspend fun refreshVoterInfoQuery(
        address: String,
        id: Int
    ) {
        withContext(Dispatchers.IO) {
            val response = api.retrofitService.getVoterInfo(address, id)
            val data = convertResponseToVoterModel(response, id )
            data?.run {
                voterInfoDatabase.dao.insertInfo(data)
            }
        }
    }

    override suspend fun loadVoterInfo(id:Int) {
        withContext(Dispatchers.IO) {
            val data = electionDatabase.electionDao.getInfo(id)
            data.run {_voterInfo.postValue(this)}
        }
    }
}