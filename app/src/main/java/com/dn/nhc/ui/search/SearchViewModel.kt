package com.dn.nhc.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.repository.remote.PheedRemoteDataSource
import com.dn.nhc.ui.pheed.PheedViewModel
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userManager: UserManager,
    private val pheedRemoteDataSource: PheedRemoteDataSource
): PheedViewModel(userManager) {

    lateinit var keyword: String

    private val _pheed = MutableLiveData<ArrayList<Pheed>>()
    val pheed: LiveData<ArrayList<Pheed>> = _pheed

    private val _showDetailEvent = MutableLiveData<Event<Pheed>>()
    val showDetailEvent: LiveData<Event<Pheed>> = _showDetailEvent

    override fun onDetailClick(item: Pheed) {
        _showDetailEvent.value = Event(item)
    }

    fun getSearchContentPheedList(keyword: String, size: Int, page: Int, lat: Double, lnt: Double){
        fetchSearchContentPheedList(keyword, size, page, lat, lnt)
    }

    fun getSearchHashTagPheedList(keyword: String, size: Int, page: Int, lat: Double, lnt: Double){
        fetchSearchHashTagPheedList(keyword, size, page, lat, lnt)
    }

    private fun fetchSearchContentPheedList(keyword: String, size: Int, page: Int, lat: Double, lnt: Double){
        viewModelScope.launch {
            val response = pheedRemoteDataSource.getSearchContentPheedListResult(keyword, size, page, lat, lnt)
            responseValidation(response)
        }
    }

    private fun fetchSearchHashTagPheedList(keyword: String, size: Int, page: Int, lat: Double, lnt: Double){
        viewModelScope.launch {
            val response = pheedRemoteDataSource.getSearchHashTagPheedListResult(keyword, size, page, lat, lnt)
            responseValidation(response)
        }
    }

}