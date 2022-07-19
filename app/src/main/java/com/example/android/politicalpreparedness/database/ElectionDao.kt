package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(election: List<Election>)

    @Query("SELECT * FROM election_table")
    fun selectAllElection(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun selectSingle(id:Int): LiveData<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM election_table")
    suspend fun clear()
}