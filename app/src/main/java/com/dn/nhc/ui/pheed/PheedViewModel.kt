package com.dn.nhc.ui.pheed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class PheedViewModel @Inject constructor(
    private val userManager: UserManager
    ): NeighborHoodChoresViewModel(userManager) {

    val onHashTagClickAction: Function1<String, Unit> = this::onHashTagClickAction

    private val _hashtagClickEvent = MutableLiveData<Event<String>>()
    val hashtagClickEvent: LiveData<Event<String>> = _hashtagClickEvent

    open fun onDetailClick(item: Pheed){}
    open fun onCommentClick(item: Pheed){}

    private fun onHashTagClickAction(keyword: String){
        _hashtagClickEvent.value = Event(keyword)
    }

}