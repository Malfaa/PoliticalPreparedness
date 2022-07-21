package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = Constants.TABLE_NAME_ELECTION)
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,
        @ColumnInfo(name = "saved")val saved: Boolean // FIXME: ideia
)

// TODO: colocar um boolean p/ distinguir se foi salvo ou não talvez