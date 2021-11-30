package com.dn.nhc.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemChildCommentBinding
import com.dn.nhc.remote.model.common.Comment
import com.lakue.lakue_library.ui.LakueViewHolder

class ChildCommentAdapter(val viewModel: CommentViewModel) : NeighborHoodChoresAdapter<Comment>(){

    val Tag = ChildCommentAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        DataBindingUtil.inflate<ItemChildCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_child_comment,
            parent,
            false
        ).let {
            return ChildCommentViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int  = items.size

    /**
     * ViewHolder
     */
    inner class ChildCommentViewHolder(private val binding: ItemChildCommentBinding) : LakueViewHolder(binding.root) {

        override fun onBind(item: Any, pos: Int) {
            binding.apply {
                this.vm = viewModel
                this.comment = item as Comment
            }
        }
    }
}