package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.election.ElectionDao
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(
    private val electionDatabase: ElectionDatabase,
    private val api: CivicsApi
) : IElectionsRepository {
    //Refresh and save as cache

    override suspend fun refreshElections() {//Active
        try{
            withContext(Dispatchers.IO) {
                val electionResponse = api.getElections()
                insertAll(electionResponse.elections)
            }

        }catch (e: Exception){
            Log.e("Refresh Elections Error", e.toString())
        }
    }

    override fun getAllSaved() = electionDatabase.electionDao.getSavedElections() //database

    override fun getAllUpcoming() = electionDatabase.electionDao.getUpcomingElections() //active  cache

    override suspend fun insertAll(elections: List<Election>) = electionDatabase.electionDao.addAll(elections) //active

    //----------------------------------------------------------------------------------------------------------

    override suspend fun getElection(id: Int): LiveData<Election> = withContext(Dispatchers.IO){ electionDatabase.electionDao.selectSingle(id)} //voterinfo


    override suspend fun addElectionToDB(election: Election){  //voterinfo
        withContext(Dispatchers.IO){
            electionDatabase.electionDao.add(election)
        }
    }

    override suspend fun removeElectionFromDB(election: Election){ //voterinfo
        withContext(Dispatchers.IO){
            electionDatabase.electionDao.deleteElection(election)
        }
    }

}