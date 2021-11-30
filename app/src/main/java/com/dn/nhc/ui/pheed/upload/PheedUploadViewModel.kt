package com.dn.nhc.ui.pheed.upload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.remote.model.common.Image
import com.dn.nhc.repository.remote.PheedRemoteDataSource
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class PheedUploadViewModel @Inject constructor(
    private val userManager: UserManager,
    private val pheedRemoteDataSource: PheedRemoteDataSource
) : CameraViewModel(userManager) {


    //TextChagne Event - keyword change
    val etGetTags: Function1<String, Unit> = this::onEditorGetTags
    val tagClickEvent: Function1<String, Unit> = this::onTagClickEvent
    val tagDeleteClickEvent: Function1<String, Unit> = this::onTagDeleteClickEvent

    val liveTags = MutableLiveData<ArrayList<String>>()
    private val tags = ArrayList<String>()

    var lastApi = ""

    //EditText Tag추가
    fun onEditorGetTags(keyword: String) {
        //키워드가 비어있을 때 처리안함
        if (keyword.isNullOrEmpty()) {
            return
        }
        //이미 있는 키워드일 경우 처리안함.
        if (tags.contains(keyword)) {
            showToast("이미 있는 해시태그입니다.")
            return
        }
        tags.add(0, keyword)
        liveTags.postValue(tags)
    }

    fun setTags(tags: ArrayList<String>) {
        this.tags.addAll(tags)
        liveTags.postValue(this.tags)
    }

    fun onTagClickEvent(keyword: String) {
        tags.remove(keyword)
        liveTags.postValue(tags)
    }

    fun onTagDeleteClickEvent(keyword: String) {
        tags.remove(keyword)
        liveTags.postValue(tags)
    }

    fun getTags(): ArrayList<String> {
        return tags
    }

    fun getPheedDetail(postId: String) {
        lastApi = "pheed_detail"
        fetchPheedDetail(postId)
    }

    fun fetchUpdatePheed(
        postId: String,
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        existingImages: List<Image>,
        partMap: HashMap<String, RequestBody>
    ){
        updatePheed(postId, files, tags, existingImages, partMap)
    }

    fun updatePheed(
        postId: String,
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        existingImages: List<Image>,
        partMap: HashMap<String, RequestBody>
    ) {
        lastApi = "pheed_update"
        viewModelScope.launch {
            val response = pheedRemoteDataSource.putUpdatePheed(postId, files, tags, existingImages, partMap)
            responseValidation(response)
        }
    }

    private fun fetchPheedDetail(postId: String) {
        viewModelScope.launch {
            val response = pheedRemoteDataSource.getPheedDetail(postId)
            responseValidation(response)
        }
    }

    fun fetchPheedUpload(
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        partMap: HashMap<String, RequestBody>
    ) {
        lastApi = "pheed_upload"
        viewModelScope.launch {
            val response = pheedRemoteDataSource.postPheedUpload(files, tags, partMap)

            responseValidation(response)
        }
    }

}