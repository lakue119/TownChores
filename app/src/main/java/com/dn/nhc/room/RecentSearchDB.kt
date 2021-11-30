package com.dn.nhc.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecentSearch::class], version = 1, exportSchema = false)
abstract class RecentSearchDB: RoomDatabase()  {
    abstract val searchDao: RecentSearchDao

    companion object {

        @Volatile
        private var INSTANCE: RecentSearchDB? = null

        fun getInstance(context: Context): RecentSearchDB {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecentSearchDB::class.java,
                        "search_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance

                }

                return instance
            }
        }
    }
}