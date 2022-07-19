package com.example.android.politicalpreparedness.repository

class ElectionsRepository(
    private val activeElectionDatabase: ActiveElectionDatabase,
    savedElectionDatabase: SavedElectionDatabase,
    private val api: CivicsApiInstance) {

        val activeElections = activeElectionDatabase.getAll()
        val savedElections = savedElectionDatabase.getAll()

        suspend fun refreshElections() {
            withContext(Dispatchers.IO) {
                val electionResponse = api.getElections()
                activeElectionDatabase.insertAll(electionResponse.elections)
            }
        }
}
