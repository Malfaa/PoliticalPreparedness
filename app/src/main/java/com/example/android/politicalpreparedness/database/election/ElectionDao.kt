package com.example.android.politicalpreparedness.database.election

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE saved = 0")
    fun getUpcomingElections(): LiveData<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(election: List<Election>)



    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE saved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE id = :id")
    fun getSingle(id:Int): LiveData<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Int)



    @Query("DELETE FROM ${Constants.TABLE_NAME_ELECTION}")
    suspend fun clear()
}