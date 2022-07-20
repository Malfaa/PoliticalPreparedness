package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.election.ElectionDao
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository( //SHOWS EXCLUSEVILY FROM THE API
    private val electionDatabase: ElectionDatabase,
    //private val savedElectionDatabase: SavedElectionDatabase,
    private val api: CivicsApi
) {
    //Active
    suspend fun insertAll(elections: List<Election>) = electionDatabase.electionDao.addAll(elections)

    fun getAll() = electionDatabase.electionDao.selectAllElection()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val electionResponse = api.getElections()
            insertAll(electionResponse.elections)
        }
    }

    //Saved

}


// TODO: FAZER OS REPOSITÓRIOS PRIMEIRO