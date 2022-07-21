package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.database.voterinfo.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepository(
    private val voterInfoDB: VoterInfoDatabase,
    private val electionDB: ElectionDatabase,
    private val api: CivicsApi
){ //Shows both data from DATABASE and from the API

    val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo : LiveData<VoterInfo>
        get() = _voterInfo

    suspend fun getInfo(id: Int): LiveData<Election> = withContext(Dispatchers.IO){ electionDB.electionDao.selectSingle(id)}

//    suspend fun addElectionToDB(election: Election){
//        withContext(Dispatchers.IO){
//            electionDB.electionDao.add(election)
//        }
//    }
//    suspend fun removeElectionFromDB(election: Election){
//        withContext(Dispatchers.IO){
//            electionDB.electionDao.deleteElection(election)
//        }
//    }

    /*
    private fun convertToVoterInfo(id: Int, response: VoterInfoResponse) : VoterInfo? {

        var voterInfo: VoterInfo? = null
        val state = response.state

        if (state?.isNotEmpty() == true) {
            val votingLocatinUrl: String? =
                state[0].electionAdministrationBody.votingLocationFinderUrl?.run {
                    this
                }

            val ballotInfoUrl: String? =
                state[0].electionAdministrationBody.ballotInfoUrl?.run {
                    this
                }

            voterInfo = VoterInfo(
                id,
                state[0].name,
                votingLocatinUrl,
                ballotInfoUrl
            )
        }

        return voterInfo
    }

    suspend fun refreshVoterInfo(address:String, id:Int) {
        withContext(Dispatchers.IO) {

            val response = api.getVoterInfo(address, id)
            val data = convertToVoterInfo(id, response)
            data?.run {
                voterInfoDatabase.insert(this)
            }
        }
    }

    suspend fun loadVoterInfo(id:Int) {
        withContext(Dispatchers.IO) {
            val data = voterInfoDatabase.get(id)
            data?.run {_voterInfo.postValue(this)}
        }
    }*/


}