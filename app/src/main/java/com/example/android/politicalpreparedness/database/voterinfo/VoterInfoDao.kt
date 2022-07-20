package com.example.android.politicalpreparedness.database.voterinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Dao
interface VoterInfoDao {
    @Query("SELECT * FROM ${Constants.TABLE_NAME_VOTER_INFO} WHERE id = :electionId")
    fun getInfo(electionId: Int) : VoterInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfo(voterInfo: VoterInfo)
}