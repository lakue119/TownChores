package com.dn.nhc.ui.pheed.upload

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityPheedUploadBinding
import com.dn.nhc.extention.toEditable
import com.dn.nhc.global.NowLocation
import com.dn.nhc.remote.model.PheedUploadResponse
import com.dn.nhc.remote.model.common.Image
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.utils.*
import com.dn.nhc.utils.camera.CameraUtil
import com.dn.nhc.utils.camera.OnShowCameraListener
import com.dn.nhc.utils.camera.OnShowGalleryListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class PheedUploadActivity :
    NeighborHoodChoresActivity<ActivityPheedUploadBinding,
            PheedUploadViewModel>(R.layout.activity_pheed_upload) {

    lateinit var cameraAdapter: PheedCameraUploadAdapter
    private lateinit var cameraUtil: CameraUtil

    private var photoUri: Uri? = null
    private var isEdit = false
    private var pheedId = -1L
    val deleteImages = ArrayList<Image>()

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraUtil.rotateBitmap(photoUri!!).let {
                        cameraAdapter.addItem(it)
                    }
                }
            }
        }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraUtil.rotateBitmap(result.data?.data!!).let {
                        cameraAdapter.addItem(it)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EXTRA_EDIT_PHEED_ID)) {
            pheedId = intent.getStringExtra(EXTRA_EDIT_PHEED_ID)?.toLong()!!
        }

        setOberver()

        cameraAdapter = PheedCameraUploadAdapter(viewModel)
        cameraAdapter.addItem(0)

        cameraUtil = CameraUtil(this, this)

        cameraUtil.apply {
            setOnShowCameraListener(object : OnShowCameraListener {
                override fun onShowCamera(uri: Uri) {
                    photoUri = uri
                    photoLauncher.launch(photoUri)
                }
            })

            setOnShowGalleryListener(object : OnShowGalleryListener {
                override fun onShowGallery(intent: Intent) {
                    albumLauncher.launch(intent)
                }
            })
        }

        if (pheedId > 0) {
            isEdit = true
            viewModel.getPheedDetail(pheedId.toString())
        }

    }

    fun setOberver() {
        viewModel.apply {
            cameraUploadEvent eventObserve { count ->
                if (count >= 5) {
                    showToast("사진은 최대 5개까지 첨부 가능합니다.")
                } else {
                    showCameraDialog()
                }
            }

            cameraDeleteEvent eventObserve { pair ->
                cameraAdapter.removePosition(pair.first)
                pair.second?.let { image ->
                    image.isDeleted = true
                    deleteImages.add(image)
                }
            }

            liveSuccess.observe(this@PheedUploadActivity) { response ->
                when (response) {
                    is PheedUploadResponse -> {
                        LoadingDialog.hideLoading(this@PheedUploadActivity)
                        when (viewModel.lastApi) {
                            "pheed_detail" -> {
                                val pheed = response.response.post
                                binding.include.headTitle =
                                    resources.getString(R.string.header_pheed_upload_edit)

                                val images = pheed.images
                                images?.let { cameraAdapter.addImageItem(ArrayList(it)) }
//                                addCameraAdapterBitmap(pheed)
                                setEditHashtag(pheed)
                                setEditData(pheed)

                            }
                            "pheed_upload" -> {
                                if (response.success) {
                                    NeighborHoodChoresUtils.liveRefreshHome.postValue(true)
                                    NeighborHoodChoresUtils.liveRefreshMypage.postValue(true)
                                    showToast("글 등록이 완료되었습니다.")
                                    finish()
                                }
                            }
                            "pheed_update" -> {
                                if (response.success) {
                                    NeighborHoodChoresUtils.liveRefreshHome.postValue(true)
                                    NeighborHoodChoresUtils.liveRefreshMypage.postValue(true)
                                    showToast("글 수정이 완료되었습니다.")
                                    val resultIntent = Intent()
                                    resultIntent.putExtra(EXTRA_UPDATE_PHEED, true)
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setEditData(pheed: Pheed) {
        binding.apply {
            etTitle.text = pheed.title?.toEditable()
            etContent.text = pheed.content?.toEditable()
        }

    }

    fun setEditHashtag(pheed: Pheed) {
        val keywords = ArrayList<String>()
        for (hashtag in pheed.hashtags!!) {
            keywords.add(hashtag.keyword!!)
        }
        viewModel.setTags(keywords)
    }

//    fun addCameraAdapterBitmap(pheed: Pheed){
//        CoroutineScope(Dispatchers.Main).launch {
//            val bitmaps = ArrayList<Bitmap>()
//            CoroutineScope(Dispatchers.IO).async {
//                pheed.images?.let { pheedImage ->
//                    for (img in pheedImage) {
//                        img.imageNameUrl?.let {
//                            bitmaps.add(BitmapUtil.getBitmapFromURL(it)!!)
//                        }
//                    }
//                }
//            }.await()
//            cameraAdapter.addBitmap(bitmaps)
//        }
//    }

    fun showCameraDialog() {
        LakueDialog.Builder(this@PheedUploadActivity)
            .setTitle("사진 첨부 방식을 선택해주세요.")
            .setAgreeText("앨범")
            .setDisagreeText("사진")
            .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                override fun onAgree(dialog: LakueDialog) {
                    cameraUtil.onShowCameraAlbum()
                    dialog.dismiss()
                }
            })
            .setDisAgreeClickListener(object : LakueDialog.OnDisagreeClickListener {
                override fun onDisagree(dialog: LakueDialog) {
                    cameraUtil.onShowCamera()
                    dialog.dismiss()
                }
            }).build().show()
    }

    @SuppressLint("MissingPermission")
    fun onPheedRegister(view: View) {
        LoadingDialog.showLoading(this@PheedUploadActivity)
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val tags = viewModel.getTags()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.apply {
                val map = HashMap<String, RequestBody>()
                val multiImages: ArrayList<MultipartBody.Part> = ArrayList()
                val multiExistingImages: ArrayList<MultipartBody.Part> = ArrayList()

                CoroutineScope(Dispatchers.Default).async {
                    for (i in 0 until getImageList().size) {
                        val rqFile: RequestBody =
                            getImageList()[i].asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val mpFile = MultipartBody.Part.createFormData("files", ".jpg", rqFile)
                        multiImages.add(mpFile)
                    }
                    map["title"] = title.toRequestBody("text/plain".toMediaTypeOrNull())
                    map["content"] = content.toRequestBody("text/plain".toMediaTypeOrNull())
                    map["latitude"] =
                        NowLocation.latitude.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull())
                    map["longitude"] =
                        NowLocation.longitude.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull())
                    map["area"] = NowLocation.area.toRequestBody("text/plain".toMediaTypeOrNull())
                }.await()
                if (isEdit) {
                    fetchUpdatePheed(
                        pheedId.toString(),
                        multiImages,
                        convertMultipleBody("hashtags", tags),
                        deleteImages,
                        map
                    )
                } else {
                    fetchPheedUpload(multiImages, convertMultipleBody("hashtags", tags), map)
                }
            }
        }
    }

    fun convertMultipleBody(name: String, list: ArrayList<String>): List<MultipartBody.Part> {
        val multipartList = ArrayList<MultipartBody.Part>()

        for (item in list) {
            multipartList.add(MultipartBody.Part.createFormData(name, item))
        }
        return multipartList
    }

    private fun getImageList(): ArrayList<File> {
        var files = ArrayList<File>()
        var images = cameraAdapter.getImages()
        val imageTypeBitmapConvertFile = ImageTypeBitmapConvertFile(this)

        for (i in 0 until images.size) {
            files.add(
                imageTypeBitmapConvertFile.BitmapConvertFile(images[i])
            )
        }

        return files
    }

    companion object {
        const val EXTRA_EDIT_PHEED_ID = "EXTRA_EDIT_PHEED_ID"
        const val EXTRA_UPDATE_PHEED = "EXTRA_UPDATE_PHEED"
    }
}