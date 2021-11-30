package com.dn.nhc.repository

import com.dn.nhc.room.RecentSearch
import com.dn.nhc.room.RecentSearchDao
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(private val recentSearchDao: RecentSearchDao) {
    suspend fun insertRecentSearchData(recentSearch: RecentSearch) = recentSearchDao.insertRecentSearch(recentSearch)
    suspend fun fetchRecentSearch() = recentSearchDao.getAll()
    suspend fun fetchValidationKeywordOverlap(keyword:String) = recentSearchDao.valiidationKeywordOverlap(keyword)
    suspend fun deleteRecentSearch(recentSearch:RecentSearch) = recentSearchDao.deleteRecentSearch(recentSearch)
    suspend fun deleteKeyword(keyword:String) = recentSearchDao.deleteKeyword(keyword)
    suspend fun deleteAll() = recentSearchDao.deleteAll()
}