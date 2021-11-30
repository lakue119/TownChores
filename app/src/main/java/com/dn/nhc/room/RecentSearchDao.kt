package com.dn.nhc.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recentsearch ORDER BY id DESC")
    fun getAll(): List<RecentSearch>

    @Query("SELECT COUNT(*) FROM recentsearch WHERE keyword = :keyword")
    fun valiidationKeywordOverlap(keyword: String): Int

    @Insert
    fun insertRecentSearch(vararg recentSearch: RecentSearch)

    @Delete
    fun deleteRecentSearch(vararg recentSearch: RecentSearch)

    @Query("DELETE FROM recentsearch")
    fun deleteAll()

    @Query("DELETE FROM recentsearch WHERE keyword = :keyword")
    fun deleteKeyword(keyword: String)
}