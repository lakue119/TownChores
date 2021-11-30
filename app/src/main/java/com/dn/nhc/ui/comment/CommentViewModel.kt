package com.dn.nhc.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.remote.model.common.Comment
import com.dn.nhc.repository.remote.PheedRemoteDataSource
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CommentViewModel @Inject constructor(
    private val userManager: UserManager,
    private val pheedRemoteDataSource: PheedRemoteDataSource
): NeighborHoodChoresViewModel(userManager) {

    private val _liveCommentEvent = MutableLiveData<Event<Comment>>()
    val liveCommentEvent: LiveData<Event<Comment>> = _liveCommentEvent

    private val _liveCommentMenuEvent = MutableLiveData<Event<Comment>>()
    val liveCommentMenuEvent: LiveData<Event<Comment>> = _liveCommentMenuEvent

    val liveComment = MutableLiveData<String>("")
    val liveCommentEmpty = MutableLiveData(false)

    var lastApi = ""

    fun onChildCommentClick(comment: Comment) {
        _liveCommentEvent.value = Event(comment)
    }

    fun getCommentList(commentId: String){
        fetchCommentList(commentId)
    }

    fun deleteComment(commentId: String){
        fetchDeleteComment(commentId)
    }

    fun postComplain(type: String, map: HashMap<String, Any>){
        fetchComplain(type, map)
    }

    fun postComment(map: HashMap<String, Any>){
        fetchPostComment(map)
    }

    private fun fetchPostComment(map: HashMap<String, Any>){
        viewModelScope.launch {
            lastApi = "insert_comment"
            val response = pheedRemoteDataSource.postComment(map)
            responseValidation(response)
        }
    }

    private fun fetchDeleteComment(commentId: String){
        viewModelScope.launch {
            lastApi = "delete_comment"
            val response = pheedRemoteDataSource.deleteComment(commentId)
            responseValidation(response)
        }
    }

    private fun fetchComplain(type: String, map: HashMap<String, Any>){
        viewModelScope.launch {
            lastApi = "complain"
            val response = pheedRemoteDataSource.postComplain(type, map)
            responseValidation(response)
        }
    }

    private fun fetchCommentList(commentId: String){
        viewModelScope.launch {
            lastApi = "get_comment"
            val response = pheedRemoteDataSource.getCommentDetail(commentId)
            responseValidation(response)
        }
    }

    fun showMenu(comment: Comment){
        _liveCommentMenuEvent.value = Event(comment)
    }
}