package com.example.android.politicalpreparedness.database.election

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Dao
interface ElectionDao {

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION}")
    fun getUpcomingElections(): LiveData<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(election: List<Election>)



    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION}")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE id = :id")
    fun getSingle(id:Int): LiveData<Election>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)


    @Query("SELECT * FROM ${Constants.TABLE_NAME_VOTER_INFO} WHERE id = :id")
    suspend fun getInfo(id:Int): VoterInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(voterInfo: VoterInfo)

    @Query("DELETE FROM ${Constants.TABLE_NAME_ELECTION}")
    suspend fun clear()
}