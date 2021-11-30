package com.dn.nhc.ui.main.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemMypageEmptyPheedBinding
import com.dn.nhc.databinding.ItemMypageInfoBinding
import com.dn.nhc.databinding.ItemPheedBinding
import com.dn.nhc.remote.model.common.User
import com.dn.nhc.ui.comment.CommentAdapter
import com.dn.nhc.ui.pheed.PheedViewHolder
import com.lakue.lakue_library.ui.LakueViewHolder

class MypageAdapter(val viewModel: MypageViewModel) : NeighborHoodChoresAdapter<Any>() {

    private val Tag = CommentAdapter::class.java.name
    private val TYPE_USER_INFO = 1001
    private val TYPE_PHEED = 1002
    private val TYPE_EMPTY_PHEED = 1003

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is User -> {
                TYPE_USER_INFO
            }
            is Int -> {
                TYPE_EMPTY_PHEED
            }
            else -> {
                TYPE_PHEED
            }
        }

//
//        return if (position == 0) {
//            TYPE_USER_INFO
//        } else {
//            if(items[position] is Int){
//                TYPE_EMPTY_PHEED
//            } else {
//                TYPE_PHEED
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        return when (viewType) {
            TYPE_USER_INFO -> {
                DataBindingUtil.inflate<ItemMypageInfoBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_mypage_info,
                    parent,
                    false
                ).let {
                    return MypageInfoViewHolder(it)
                }
            }
            TYPE_EMPTY_PHEED -> {
                DataBindingUtil.inflate<ItemMypageEmptyPheedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_mypage_empty_pheed,
                    parent,
                    false
                ).let {
                    return EmptyPheedViewHolder(it)
                }
            }
            else -> {
                DataBindingUtil.inflate<ItemPheedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_pheed,
                    parent,
                    false
                ).let {
                    return PheedViewHolder(it, viewModel)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class MypageInfoViewHolder(
        private val binding: ItemMypageInfoBinding
    ) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, pos: Int) {
            binding.apply {
                this.vm = viewModel
                this.user = item as User
            }
        }
    }

    inner class EmptyPheedViewHolder(
        private val binding: ItemMypageEmptyPheedBinding
    ) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, pos: Int) {
            binding.apply {
                this.vm = viewModel
            }
        }
    }
}