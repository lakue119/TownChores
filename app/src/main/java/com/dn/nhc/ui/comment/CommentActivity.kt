package com.dn.nhc.ui.comment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityCommentBinding
import com.dn.nhc.remote.model.CommentListResponse
import com.dn.nhc.remote.model.CommentResponse
import com.dn.nhc.remote.model.CommonResponse
import com.dn.nhc.remote.model.common.Comment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lakue.lakue_library.ext.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentActivity :
    NeighborHoodChoresActivity<ActivityCommentBinding, CommentViewModel>(R.layout.activity_comment) {

    lateinit var commentAdapter: CommentAdapter
    lateinit var bottomSheetDialog: BottomSheetDialog
    val commentId by lazy { intent.getIntExtra(EXTRA_COMMENT_ID,-1)}
    var isCommentChange = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commentAdapter = CommentAdapter(viewModel)
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_bottom_menu_popup, null)
        bottomSheetDialog = BottomSheetDialog(this, R.style.NewDialog)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.setContentView(bottomSheetView)
        viewModel.apply{
            getCommentList(commentId.toString())
            liveCommentMenuEvent eventObserve { showCommentMenu(it) }
            liveComment.observe(this@CommentActivity) {
                liveCommentEmpty.postValue(it.isEmpty())
            }
        }
        setObserver()
    }



    private fun setObserver() {
        viewModel.apply {
            liveSuccess.observe(this@CommentActivity) { response ->

                when(response){
                    is CommentListResponse -> {
                        val comments = response.response.comments
                        if(comments?.isNotEmpty()!!){
                            binding.item = comments[0]
                            for(comment in comments){
                                commentAdapter.addItem(comment)
                            }
                        }
                    }

                    is CommentResponse -> {
                        isCommentChange = true
                        val comment = response.response.comment
                        commentAdapter.addChildComment(comment!!)
                        viewModel.liveComment.value = ""
                        //TODO 대댓글 작성 후 맨 아래로 가도록 수정...
                        commentAdapter.showLastComment()
                        hideKeyboard()
                    }
                    is CommonResponse -> {
                        if(response.response){
                            bottomSheetDialog.dismiss()
                            when(lastApi){
                                "delete_comment" -> {
                                    isCommentChange = true
                                    showToast("댓글이 삭제되었습니다.")
                                    activityRefresh()
                                }

                                "complain" -> {
                                    showToast("신고가 접수되었습니다.")
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    fun showCommentMenu(comment: Comment){
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
            viewModel.postComplain("1", map)
        }

        btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    fun editComment(comment: Comment){
        val map = HashMap<String, Any>()
        map["post_id"] = comment.postId
        map["comment"] = viewModel.liveComment.value!!
        map["parent_comment_id"] = comment.id
        viewModel.postComment(map)
    }

    override fun finish() {
        val resultIntent = Intent()
        resultIntent.putExtra("is_comment_change", isCommentChange)
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }
    companion object{
        const val EXTRA_COMMENT_ID = "EXTRA_COMMENT_ID"
    }

}