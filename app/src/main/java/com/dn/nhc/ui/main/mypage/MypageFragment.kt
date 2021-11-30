package com.dn.nhc.ui.main.mypage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentMypageBinding
import com.dn.nhc.remote.model.CommonResponse
import com.dn.nhc.remote.model.LoginResponse
import com.dn.nhc.remote.model.PheedResponse
import com.dn.nhc.remote.model.UserResponse
import com.dn.nhc.ui.login.LoginActivity
import com.dn.nhc.ui.pheed.detail.PheedDetailActivity
import com.dn.nhc.ui.pheed.upload.PheedUploadActivity
import com.dn.nhc.utils.ImageTypeBitmapConvertFile
import com.dn.nhc.utils.LakueDialog
import com.dn.nhc.utils.LoadingDialog
import com.dn.nhc.utils.NeighborHoodChoresUtils
import com.dn.nhc.utils.camera.CameraUtil
import com.dn.nhc.utils.camera.OnShowCameraListener
import com.dn.nhc.utils.camera.OnShowGalleryListener
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.ui.LakueActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class MypageFragment : NeighborHoodChoresFragment<FragmentMypageBinding, MypageViewModel>(R.layout.fragment_mypage) {

    lateinit var mypageAdapter: MypageAdapter
    private var page = 1
    val ROAD_COUNT = 20

    val TYPE_ALBUM = 1001
    val TYPE_CAMERA = 1002
    var selectType = TYPE_ALBUM

    val rvBottomCatch: Function1<Int, Unit> = this::onBottomCatch

    private lateinit var cameraUtil: CameraUtil

    private var photoUri: Uri? = null

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraUtil.rotateBitmap(photoUri!!).let { bitmap ->
                        updateProfileImage(bitmap)
                    }
                }
            }
        }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraUtil.rotateBitmap(result.data?.data!!).let { bitmap ->
                        updateProfileImage(bitmap)
                    }
                }
            }
        }


    companion object {

        @JvmStatic
        fun newInstance() =
            MypageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypageAdapter = MypageAdapter(viewModel)
        cameraUtil = CameraUtil(requireContext(), requireActivity())
        LoadingDialog.showLoading(requireActivity() as LakueActivity<*, *>)

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            showDetailEvent eventObserve { item ->
                showDetailPheed(item.id!!)
            }
            updateProfileImage eventObserve {
                showCameraDialog()
            }

            uploadPheedEvent eventObserve {
                (activity as LakueActivity<*, *>).startActivity<PheedUploadActivity>()
            }

        }
        refreshMypage()
        setObserver()
    }

    private fun updateProfileImage(bitmap: Bitmap){
        val imageTypeBitmapConvertFile = ImageTypeBitmapConvertFile(requireContext())
        val file = imageTypeBitmapConvertFile.BitmapConvertFile(bitmap)

        val rqFile: RequestBody =  file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val mpFile = MultipartBody.Part.createFormData("files", ".jpg", rqFile)

        viewModel.putProfileImage(mpFile)
    }

    private fun setObserver() {

        NeighborHoodChoresUtils.liveRefreshMypage.observe(viewLifecycleOwner) {
            if(it && !rvloading && mypageAdapter.itemCount > 0){
                refreshMypage()
                NeighborHoodChoresUtils.liveRefreshMypage.postValue(false)
            }
        }

        viewModel.apply {
            liveSuccess.observe(viewLifecycleOwner) { response ->
                LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
                if(response == null){
                    rvloading = false
                    return@observe
                }
                when(response){
                    is PheedResponse -> {
                        val pheeds = response.response.posts
                        page++
                        if(pheeds.isEmpty() && mypageAdapter.itemCount <= 1){
                            mypageAdapter.addItem(1001)
                            isLastPage = true
                        } else {
                            for(pheed in pheeds){
                                mypageAdapter.addItem(pheed)
                            }
                        }
                        rvloading = false
                    }
                    is LoginResponse -> {
                        val user = response.response?.user

                        user?.let { mypageAdapter.addItem(it) }
                        getMyPheedList(ROAD_COUNT, page)

                    }
                    is UserResponse -> {
                        refreshMypage()
                    }
                    is CommonResponse -> {
                        viewModel.dbLogout()
                        (activity as LakueActivity<*, *>).startActivity<LoginActivity>()
                        (activity as LakueActivity<*, *>).finish()
                    }
                }

            }
        }
    }

    fun sessionLogin(){
        viewModel.sessionLogout()
    }

    fun refreshMypage(){
        page = 1
        isLastPage = false
        rvloading = true
        mypageAdapter.clear()
        viewModel.getUserInfo()
    }

    //RecyclerView Bottom Catch
    private fun onBottomCatch(lastPosition: Int) {
        if (!rvloading && lastPosition >= mypageAdapter.itemCount - 2) {
            getPheedList()
        }
    }

    private fun showCameraDialog() {
        LakueDialog.Builder(requireContext())
            .setTitle("사진 첨부 방식을 선택해주세요.")
            .setAgreeText("앨범")
            .setDisagreeText("사진")
            .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                override fun onAgree(dialog: LakueDialog) {
                    selectType = TYPE_ALBUM
                    cameraUtil.onShowCameraAlbum()
                    dialog.dismiss()
                }
            })
            .setDisAgreeClickListener(object : LakueDialog.OnDisagreeClickListener {
                override fun onDisagree(dialog: LakueDialog) {
                    selectType = TYPE_CAMERA
                    cameraUtil.onShowCamera()
                    dialog.dismiss()
                }
            }).build().show()
    }

    fun showCameraPermission(){
        when(selectType){
            TYPE_CAMERA -> {cameraUtil.onShowCamera()}
            TYPE_ALBUM -> {cameraUtil.onShowCameraAlbum()}
        }
    }

    fun getPheedList() {
        if(isLastPage){
           return
        }
        rvloading = true
        viewModel.getMyPheedList(
            ROAD_COUNT,
            page
        )
    }

    fun showDetailPheed(pheedId: Long) {
        (activity as LakueActivity<*, *>).startActivity<PheedDetailActivity>(Pair(
            PheedDetailActivity.EXTRA_PHEED_ID, pheedId.toString()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}