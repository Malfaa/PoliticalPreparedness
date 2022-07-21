package com.example.android.politicalpreparedness.database.election

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(election: List<Election>)

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE saved = 0")
    fun getUpcomingElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE saved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME_ELECTION} WHERE id = :id")
    fun selectSingle(id:Int): LiveData<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM ${Constants.TABLE_NAME_ELECTION}")
    suspend fun clear()
}