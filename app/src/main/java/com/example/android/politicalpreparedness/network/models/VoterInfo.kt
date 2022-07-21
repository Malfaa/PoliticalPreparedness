package com.example.android.politicalpreparedness.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.Constants

@Entity(tableName = Constants.TABLE_NAME_VOTER_INFO)
data class VoterInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val address: String,
    val locationUrl: String,
    val ballotInformationUrl: String
)
