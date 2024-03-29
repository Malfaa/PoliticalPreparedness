package com.example.android.politicalpreparedness.database.election

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.database.Converters
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Database(entities = [Election::class, VoterInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UpcomingElectionDatabase: RoomDatabase() {

    abstract val dao: ElectionDao

    companion object {
        @Volatile
        private var INSTANCE: UpcomingElectionDatabase? = null

        fun getInstance(context: Context): UpcomingElectionDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UpcomingElectionDatabase::class.java,
                        Constants.TABLE_NAME_UPCOMING_ELECTION
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