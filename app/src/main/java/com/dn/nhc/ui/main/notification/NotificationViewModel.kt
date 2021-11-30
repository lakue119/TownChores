package com.dn.nhc.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.remote.model.common.Pushes
import com.dn.nhc.repository.remote.MypageRemoteDataSource
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userManager: UserManager,
    private val mypageRemoteDataSource: MypageRemoteDataSource
): NeighborHoodChoresViewModel(userManager) {

    private val _showDetailEvent = MutableLiveData<Event<Pair<Pushes, Int>>>()
    val showDetailEvent: LiveData<Event<Pair<Pushes, Int>>> = _showDetailEvent

    fun getNotificationList(size: Int, page: Int){
        fetchNotificationList(size, page)
    }

    private fun fetchNotificationList(size: Int, page: Int){
        viewModelScope.launch {
            val response = mypageRemoteDataSource.getPushes(size, page)
            responseValidation(response)
        }
    }

    private fun fetchReadPush(pushId: Int){
        viewModelScope.launch {
            val response = mypageRemoteDataSource.patchReadPush(pushId)
            responseValidation(response)
        }
    }

    fun onDetailClick(item: Pushes, position: Int) {
        fetchReadPush(item.id)
        _showDetailEvent.value = Event(Pair(item, position))
    }

}