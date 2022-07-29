package com.example.android.politicalpreparedness.database.voterinfo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.database.Converters
import com.example.android.politicalpreparedness.database.election.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Database(entities = [VoterInfo::class, Election::class], version = 1, exportSchema = false) //alterei
@TypeConverters(Converters::class)
abstract class VoterInfoDatabase : RoomDatabase() {

    abstract val dao: ElectionDao

    companion object {
        @Volatile
        private var INSTANCE: VoterInfoDatabase? = null

        fun getInstance(context: Context): VoterInfoDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        VoterInfoDatabase::class.java,
                        Constants.TABLE_NAME_VOTER_INFO
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}