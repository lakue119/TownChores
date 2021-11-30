package com.dn.nhc.ui.main.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.repository.remote.MypageRemoteDataSource
import com.dn.nhc.repository.remote.UserRemoteDataSource
import com.dn.nhc.ui.pheed.PheedViewModel
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val mypageRemoteDataSource: MypageRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userManager: UserManager
): PheedViewModel(userManager) {

    private val _showDetailEvent = MutableLiveData<Event<Pheed>>()
    val showDetailEvent: LiveData<Event<Pheed>> = _showDetailEvent

    private val _updateProfileImage = MutableLiveData<Event<Boolean>>()
    val updateProfileImage: LiveData<Event<Boolean>> = _updateProfileImage

    private val _uploadPheedEvent = MutableLiveData<Event<Boolean>>()
    val uploadPheedEvent: LiveData<Event<Boolean>> = _uploadPheedEvent

    fun getUserInfo(){
        fetchUserInfo()
    }

    fun getMyPheedList(size: Int, page: Int){
        fetchPheedList(size, page)
    }

    fun putProfileImage(files: MultipartBody.Part){
        fetchUpdateProfileImage(files)
    }

    fun sessionLogout(){
        fetchLogout()
    }

    private fun fetchUserInfo(){
        viewModelScope.launch {
            val response = mypageRemoteDataSource.getMypageInfo()
            responseValidation(response)
        }
    }

    private fun fetchPheedList(size: Int, page: Int){
        viewModelScope.launch {
            val response = mypageRemoteDataSource.getMypagePheed(size, page)
            responseValidation(response)
        }
    }

    private fun fetchUpdateProfileImage(files: MultipartBody.Part){
        viewModelScope.launch {
            val response = mypageRemoteDataSource.putUpdateProfileImage(files)
            responseValidation(response)
        }
    }

    private fun fetchLogout(){
        viewModelScope.launch {
            val response = userRemoteDataSource.postLogout()
            responseValidation(response)
        }
    }

    override fun onDetailClick(item: Pheed) {
        _showDetailEvent.value = Event(item)
    }

    fun onUpdateProfileUpdate(){
        _updateProfileImage.value = Event(true)
    }

    fun startUploadPheed(){
        _uploadPheedEvent.value = Event(true)
    }

}