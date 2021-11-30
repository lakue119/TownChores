package com.dn.nhc.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemParentCommentBinding
import com.dn.nhc.remote.model.common.Comment
import com.lakue.lakue_library.ui.LakueViewHolder


class CommentAdapter(val viewModel: CommentViewModel) : NeighborHoodChoresAdapter<Comment>() {
    val Tag = CommentAdapter::class.java.name
    lateinit var viewHolder: CommentViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        DataBindingUtil.inflate<ItemParentCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_parent_comment,
            parent,
            false
        ).let {
            viewHolder = CommentViewHolder(it)
            return viewHolder
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int  = items.size

    fun addChildComment(comment: Comment){
        viewHolder.addChildComment(comment)
    }

    fun showLastComment(){
        viewHolder.showLastComment()
    }

    /**
     * ViewHolder
     */
    inner class CommentViewHolder(private val binding: ItemParentCommentBinding) : LakueViewHolder(binding.root) {

        var childCommentAdapter: ChildCommentAdapter = ChildCommentAdapter(viewModel)

        override fun onBind(item: Any, position: Int) {
            binding.apply {
                this.viewholder = this@CommentViewHolder
                this.comment = item as Comment
                this.vm = viewModel
                childCommentAdapter.clear()

                item.childComments?.let{ comment ->
                    rvChildComment.visibility = View.VISIBLE
                    childCommentAdapter.addItem(comment)
                }
            }
        }

        fun addChildComment(comment: Comment){
            binding.rvChildComment.visibility = View.VISIBLE
            childCommentAdapter.addItem(comment)
        }

        fun showLastComment(){
            binding.rvChildComment.post{
                binding.rvChildComment.scrollToPosition(childCommentAdapter.itemCount -1)
            }
        }
    }
}