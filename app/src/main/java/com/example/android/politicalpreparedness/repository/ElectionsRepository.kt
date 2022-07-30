package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.database.election.UpcomingElectionDatabase
import com.example.android.politicalpreparedness.database.voterinfo.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(
    private val electionDatabase: ElectionDatabase,
    private val voterInfoDatabase: VoterInfoDatabase,
    private val upcomingDatabase: UpcomingElectionDatabase,
    private val api: CivicsApi
) : IElectionsRepository {


    //UPCOMING
    val upcomingElections = upcomingDatabase.dao.getElections()

    override suspend fun refreshElections() {
        try {
            withContext(Dispatchers.IO) {
                val electionResponse = api.retrofitService.getElections()
                upcomingDatabase.dao.addAll(electionResponse.elections)
            }

        } catch (e: Exception) {
            Log.e("Refresh Elections Error", e.toString())
        }
    }

    //ELECTION
    val savedElections = electionDatabase.dao.getElections()

    override suspend fun getElection(id: Int): Election {
        val election = withContext(Dispatchers.IO) {
            return@withContext electionDatabase.dao.getSingle(id)
        }
        return election
    }


    override suspend fun addElectionToDB(election: Election) {
        withContext(Dispatchers.IO) {
            electionDatabase.dao.addElection(election)
        }
    }

    override suspend fun removeElectionFromDB(election: Election) {
        withContext(Dispatchers.IO) {
            electionDatabase.dao.deleteElection(election)
        }
    }





    //VOTERINFO
    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

    //This could be directly used via databinding in xml
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
            val data = voterInfoDatabase.dao.getInfo(id)
            data.run {_voterInfo.postValue(this)}
        }
    }
}