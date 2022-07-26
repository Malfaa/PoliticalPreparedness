package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.Constants

@Entity(tableName = Constants.TABLE_NAME_VOTER_INFO)
data class VoterInfo(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "state")
    val state: String, //fixme address
    @ColumnInfo(name = "location_url")
    val locationUrl: String,
    @ColumnInfo(name = "ballot_url")
    val ballotInformationUrl: String
)
