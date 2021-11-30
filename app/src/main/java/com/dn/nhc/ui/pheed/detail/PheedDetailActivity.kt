package com.dn.nhc.ui.pheed.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityPheedDetailBinding
import com.dn.nhc.decoration.PheedGridDecoration
import com.dn.nhc.remote.model.CommentListResponse
import com.dn.nhc.remote.model.CommentResponse
import com.dn.nhc.remote.model.CommonResponse
import com.dn.nhc.remote.model.PheedUploadResponse
import com.dn.nhc.remote.model.common.Comment
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.ui.comment.CommentActivity
import com.dn.nhc.ui.comment.CommentActivity.Companion.EXTRA_COMMENT_ID
import com.dn.nhc.ui.comment.CommentAdapter
import com.dn.nhc.ui.common.ActivityImageSlider
import com.dn.nhc.ui.pheed.upload.PheedUploadActivity
import com.dn.nhc.utils.ActivityContract
import com.dn.nhc.utils.LoadingDialog
import com.dn.nhc.utils.NeighborHoodChoresUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lakue.lakue_library.ext.hideKeyboard
import com.lakue.lakue_library.ext.showKeyboard
import com.lakue.lakue_library.ext.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PheedDetailActivity :
    NeighborHoodChoresActivity<ActivityPheedDetailBinding, PheedDetailViewModel>(R.layout.activity_pheed_detail) {

    var pheedId = -1L

    lateinit var imageAdapter: PheedImageGridViewAdapter
    lateinit var commentAdapter: CommentAdapter

    var pheedImages = ArrayList<String>()
    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (pheedId < 0) {
            pheedId = intent?.getStringExtra(EXTRA_PHEED_ID)?.toLong()!!
        }

        initBottomDialog()


        viewModel.apply {
            imageAdapter = PheedImageGridViewAdapter(this)
            commentAdapter = CommentAdapter(this)
            liveCommentEvent eventObserve { showComment(it) }
            liveCommentMenuEvent eventObserve { showCommentMenu(it) }
            showImageDetetailEvent eventObserve {
                startActivity<ActivityImageSlider>(
                    Pair(ActivityImageSlider.EXTRA_IMGS, it.first),
                    Pair(ActivityImageSlider.EXTRA_POSITION, it.second)
                )
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            liveComment.observe(this@PheedDetailActivity) {
                liveCommentEmpty.postValue(it.isEmpty())
            }

            getPheedDetail(pheedId.toString())
        }
        setObserver()
        setGridSpan()

        binding.ncRvComment.setEmptyView(binding.includeEmpty.clEmpty)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(EXTRA_PHEED_ID, pheedId)
        super.onSaveInstanceState(outState)
    }

    override fun setSavedInstanceState(savedInstanceState: Bundle?) {
        val bundle = getBundle(savedInstanceState)

        bundle?.getString(EXTRA_PHEED_ID)?.let {
            pheedId = it.toLong()
        } ?: run {
            // EXTRA_NAME 정보 없음 오류로그 남기기
            // EXTRA_NAME
            finish()
        }
    }

    private fun setObserver() {
        viewModel.apply {
            liveSuccess.observe(this@PheedDetailActivity) { response ->
                when (response) {
                    is PheedUploadResponse -> {
                        val pheed = response.response.post

                        pheed.images?.let { pheedImage ->
                            for (img in pheedImage) {
                                img.imageNameUrl?.let { pheedImages.add(it) }
                            }
                        }

                        binding.apply {
                            item = pheed
                            rvImage.addItemDecoration(PheedGridDecoration(pheedImages.size))
                        }
                        imageAdapter.addItem(pheed.images!!)
                        getComment(pheedId.toString())
                    }
                    is CommentListResponse -> {
                        val comments = response.response.comments

                        comments?.let { commentList ->
                            for (comment in commentList) {
                                commentAdapter.addItem(comment)
                            }
                            binding.ncRvComment.setIsDataSetting(true)
                        }
                    }
                    is CommentResponse -> {
                        val comment = response.response.comment
                        comment?.let { commentAdapter.addItem(it) }
                        binding.apply {
                            nscPheedDetail.post {
                                binding.nscPheedDetail.fullScroll(ScrollView.FOCUS_DOWN)
                            }
                            viewModel.liveComment.value = ""
                        }
                        hideKeyboard()
                    }
                    is CommonResponse -> {
                        if (response.response) {
                            bottomSheetDialog.dismiss()
                            when (lastApi) {
                                "delete_comment" -> {
                                    showToast(getString(R.string.delete_comment))
                                    activityRefresh()
                                }

                                "complain" -> {
                                    showToast(getString(R.string.complain_success))
                                }

                                "delete_pheed" -> {
                                    showToast(getString(R.string.delete_pheed))
                                    NeighborHoodChoresUtils.liveRefreshHome.postValue(true)
                                    NeighborHoodChoresUtils.liveRefreshMypage.postValue(true)
                                    finish()
                                }
                            }

                        }
                    }
                }
                LoadingDialog.hideLoading(this@PheedDetailActivity)
            }
        }
    }

    fun initBottomDialog() {
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_bottom_menu_popup, null)
        bottomSheetDialog = BottomSheetDialog(this, R.style.NewDialog)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.setContentView(bottomSheetView)
    }

    fun setGridSpan() {
        (binding.rvImage.layoutManager as GridLayoutManager).spanSizeLookup =
            object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    when (pheedImages.size) {
//                        0 -> {
//                        }
//                        1 -> {
//                            return 6
//                        }
//                        2 -> {
//                            return 3
//                        }
//                        3 -> {
//                            when (position) {
//                                0 -> return 6
//                                1 -> return 3
//                                2 -> return 3
//                            }
//                        }
//                        4 -> {
//                            return 3
//                        }
//                        else -> {
//                            when (position) {
//                                0 -> return 3
//                                1 -> return 3
//                                2 -> return 2
//                                3 -> return 2
//                                4 -> return 2
//                            }
//                        }
                        0 -> {
                        }
                        1 -> {
                            return 4
                        }
                        2 -> {
                            return 2
                        }
                        3 -> {
                            when (position) {
                                0 -> return 4
                                1 -> return 2
                                2 -> return 2
                            }
                        }
                        else -> {
                            when (position) {
                                0 -> return 2
                                1 -> return 2
                                2 -> return 2
                                3 -> return 2
                            }
                        }
                    }
                    return imageAdapter.getItemViewType(position)
                }
            }
    }

    private var commentLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityContract()
    ) {
        it?.let {
            val isCommentChange = it.getBooleanExtra("is_comment_change", false)
            if (isCommentChange) {
                commentAdapter.clear()
                viewModel.getComment(pheedId.toString())
            }
        }.run {

        }
    }

    private var pheedUpdateLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityContract()
    ) {
        it?.let {
            val isPheedUpdate = it.getBooleanExtra(PheedUploadActivity.EXTRA_UPDATE_PHEED, false)
            if (isPheedUpdate) {
                activityRefresh()
            }
        }.run {

        }
    }

    fun showComment(comment: Comment) {
        val intent = Intent(this@PheedDetailActivity, CommentActivity::class.java)
        intent.putExtra(EXTRA_COMMENT_ID, comment.id)
        commentLauncher.launch(intent)
    }

    fun showCommentMenu(comment: Comment) {
        bottomSheetDialog.show()

        val btnDelete = bottomSheetDialog.findViewById<TextView>(R.id.tvDelete)
        val btnComplain = bottomSheetDialog.findViewById<TextView>(R.id.tvComplain)
        val btnCancel = bottomSheetDialog.findViewById<TextView>(R.id.tvCancel)

        btnDelete?.isVisible = comment.isMine
        btnComplain?.isVisible = !comment.isMine

        btnDelete?.setOnClickListener {
            viewModel.deleteComment(comment.id.toString())
        }

        btnComplain?.setOnClickListener {
            val map = HashMap<String, Any>()
            map["target_id"] = comment.id
            map["target_user_id"] = comment.user.id!!
            viewModel.postComplain(ComplainType.COMMENT.type, map)
        }

        btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    fun editComment(view: View, pheedId: Long) {
        LoadingDialog.showLoading(this@PheedDetailActivity)
        val map = HashMap<String, Any>()
        map["post_id"] = pheedId
        map["comment"] = viewModel.liveComment.value!!
        viewModel.postComment(map)
    }

    fun showMenu(pheed: Pheed) {
        bottomSheetDialog.show()

        val btnDelete = bottomSheetDialog.findViewById<TextView>(R.id.tvDelete)
        val btnComplain = bottomSheetDialog.findViewById<TextView>(R.id.tvComplain)
        val btnCancel = bottomSheetDialog.findViewById<TextView>(R.id.tvCancel)
        val btnEdit = bottomSheetDialog.findViewById<TextView>(R.id.tvEdit)

        pheed.isMine?.let {
            btnDelete?.isVisible = it
            btnComplain?.isVisible = !it
            btnEdit?.isVisible = it
        }

        btnEdit?.setOnClickListener {
            val intent = Intent(this@PheedDetailActivity, PheedUploadActivity::class.java)
            intent.putExtra(PheedUploadActivity.EXTRA_EDIT_PHEED_ID, pheed.id.toString())
            pheedUpdateLauncher.launch(intent)
//            startActivity<PheedUploadActivity>(
//                Pair(
//                    PheedUploadActivity.EXTRA_EDIT_PHEED_ID, pheed.id.toString()
//                )
//            )
        }

        btnDelete?.setOnClickListener {
            LoadingDialog.showLoading(this@PheedDetailActivity)
            viewModel.deletePheed(pheed.id.toString())
        }

        btnComplain?.setOnClickListener {
            LoadingDialog.showLoading(this@PheedDetailActivity)
            val map = HashMap<String, Any>()
            map["target_id"] = pheed.id!!
            map["target_user_id"] = pheed.user?.id!!
            viewModel.postComplain(ComplainType.PHEED.type, map)
        }

        btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    fun showCommentKeyboard() {
        binding.etComment.requestFocus()
        showKeyboard()
    }

    companion object {
        const val EXTRA_PHEED_ID = "EXTRA_PHEED_ID"
        private const val QUERY_PHEED_ID = "pheed_id"

        private fun getIntent(context: Context, name: String) =
            Intent(context, PheedDetailActivity::class.java).apply {
                putExtra(EXTRA_PHEED_ID, name)
            }

        fun getIntent(context: Context, deepLink: Uri): Intent {
            val name = deepLink.getQueryParameter(QUERY_PHEED_ID) ?: run {
                // 딥링크 name 파라미터 없음 오류 로그남기기
                // DeepLink does'nt have name query parameter
                ""
            }
            return getIntent(context, name)
        }
    }


    enum class ComplainType(val type: String) {
        PHEED("1"),
        COMMENT("2"),
        USER("3")
    }

}