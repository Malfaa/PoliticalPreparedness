package com.example.android.politicalpreparedness.database.election

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Dao
interface ElectionDao {

    //UPCOMING

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION}")
    fun getElections(): LiveData<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(election: List<Election>)




    //ELECTIONS

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE id = :id")
    suspend fun getSingle(id:Int): Election

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM ${Constants.TABLE_NAME_ELECTION}")
    suspend fun clear()




    //VOTERINFO

    @Query("SELECT * FROM ${Constants.TABLE_NAME_VOTER_INFO} WHERE id = :id")
    suspend fun getInfo(id:Int): VoterInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(voterInfo: VoterInfo)


}