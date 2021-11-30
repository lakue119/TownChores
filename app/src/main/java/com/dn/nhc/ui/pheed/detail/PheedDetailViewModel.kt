package com.dn.nhc.ui.pheed.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.R
import com.dn.nhc.remote.model.common.Image
import com.dn.nhc.repository.remote.PheedRemoteDataSource
import com.dn.nhc.ui.comment.CommentViewModel
import com.dn.nhc.utils.BaseUtils.context
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PheedDetailViewModel @Inject constructor(
    private val userManager: UserManager,
    val pheedRemoteDataSource: PheedRemoteDataSource
): CommentViewModel(userManager, pheedRemoteDataSource) {

    private val _showImageDetetailEvent = MutableLiveData<Event<Pair<ArrayList<Image>, Int>>>()
    val showImageDetetailEvent: LiveData<Event<Pair<ArrayList<Image>, Int>>> = _showImageDetetailEvent

    fun showImageDetailClick(images: ArrayList<Image>, position: Int){
        _showImageDetetailEvent.value = Event(Pair(images, position))
    }

    fun getPheedDetail(postId: String){
        fetchPheedDetail(postId)
    }

    fun getComment(postId: String){
        fetchComment(postId)
    }

    fun deletePheed(postId: String){
        fetchDeletePheed(postId)
    }

    private fun fetchPheedDetail(postId: String){
        viewModelScope.launch {
            val response = pheedRemoteDataSource.getPheedDetail(postId)
            responseValidation(response)
        }
    }

    private fun fetchComment(postId: String){
        viewModelScope.launch {
            val response = pheedRemoteDataSource.getComment(postId)
            responseValidation(response)
        }
    }

    private fun fetchDeletePheed(postId: String){
        viewModelScope.launch {
            lastApi = "delete_pheed"
            val response = pheedRemoteDataSource.deletePheed(postId)
            responseValidation(response)
        }
    }
    fun getCount(count: Int): String{
        return context.getString(R.string.pheed_count, count)
    }
}