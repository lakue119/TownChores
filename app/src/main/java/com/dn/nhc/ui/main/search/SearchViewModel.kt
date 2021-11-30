package com.dn.nhc.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.repository.RecentSearchRepository
import com.dn.nhc.room.RecentSearch
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userManager: UserManager,
    private val repository: RecentSearchRepository
): NeighborHoodChoresViewModel(userManager) {

    val liveKeyword = MutableLiveData<String>("")
    val liveKeywordEmpty = MutableLiveData(false)

    private val _deleteKeywordEnent = MutableLiveData<Event<String>>()
    val deleteKeywordEnent: LiveData<Event<String>> = _deleteKeywordEnent

    private val _keywordEvent = MutableLiveData<Event<String>>()
    val keywordEvent: LiveData<Event<String>> = _keywordEvent

    suspend fun getKeyword(): List<RecentSearch>{
        lateinit var keywords: List<RecentSearch>
        CoroutineScope(Dispatchers.IO).async {
            keywords = repository.fetchRecentSearch()
        }.await()
        return keywords
    }

    suspend fun setKeyword(keyword: String){
        withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
            if (repository.fetchValidationKeywordOverlap(keyword) > 0) {
                repository.deleteKeyword(keyword)
                repository.insertRecentSearchData(RecentSearch(0, keyword))
            } else {
                repository.insertRecentSearchData(RecentSearch(0, keyword))
            }
        }
    }

    fun deleteKeyword(keyword: String){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                repository.deleteKeyword(keyword)
            }
            _deleteKeywordEnent.value = Event(keyword)
        }
    }

    fun deleteAll(){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                repository.deleteAll()
            }
            _deleteKeywordEnent.value = Event("0")
        }
    }

    fun onKeywordClick(keyword: String){
        _keywordEvent.value = Event(keyword)
    }
}