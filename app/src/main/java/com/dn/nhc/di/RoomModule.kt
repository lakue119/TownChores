package com.dn.nhc.di

import android.content.Context
import com.dn.nhc.repository.RecentSearchRepository
import com.dn.nhc.room.RecentSearchDB
import com.dn.nhc.room.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideRecentSearchDao(@ApplicationContext appContext: Context): RecentSearchDao{
        return RecentSearchDB.getInstance(appContext).searchDao
    }

    @Provides
    fun provideRecentSearchDBRepository(recentSearchDao: RecentSearchDao) = RecentSearchRepository(recentSearchDao)

}