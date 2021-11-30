package com.dn.nhc.ui.pheed.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.remote.model.common.Image
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CameraViewModel @Inject constructor(
    private val userManager: UserManager
    ): NeighborHoodChoresViewModel(userManager) {

    private val _cameraUploadEvent = MutableLiveData<Event<Int>>()
    val cameraUploadEvent: LiveData<Event<Int>> = _cameraUploadEvent

    private val _cameraDeleteEvent = MutableLiveData<Event<Pair<Int, Image?>>>()
    val cameraDeleteEvent: LiveData<Event<Pair<Int, Image?>>> = _cameraDeleteEvent

    fun onCameraUploadClick(count: Int){
        _cameraUploadEvent.value = Event(count)
    }

    fun onCameraDeleteClick(position: Int, item: Image?){
        _cameraDeleteEvent.value = Event(Pair(position, item))
    }

    fun getMaxCountText(count: Int): String{
        return "$count/5"
    }
}