package com.dn.nhc.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemSearchBinding
import com.dn.nhc.databinding.ItemSearchHeaderBinding
import com.dn.nhc.room.RecentSearch
import com.lakue.lakue_library.ui.LakueViewHolder

class SearchAdapter(private val viewModel: SearchViewModel) : NeighborHoodChoresAdapter<Any>(){

    val TYPE_HEADER = 1001
    val TYPE_ITEM = 1002

    val Tag = SearchAdapter::class.java.name

    override fun getItemViewType(position: Int): Int {
        return if(items[position] is Int){
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        when(viewType){
            TYPE_HEADER -> {
                DataBindingUtil.inflate<ItemSearchHeaderBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_header,
                    parent,
                    false
                ).let {
                    return SearchHeaderViewHolder(it)
                }
            }
            else -> {
                DataBindingUtil.inflate<ItemSearchBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search,
                    parent,
                    false
                ).let {
                    return SearchViewHolder(it)
                }
            }
        }

    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        when(holder){
            is SearchViewHolder -> {
                holder.onBind(items[position], position)
            }
            is SearchHeaderViewHolder -> {
                holder.onBind(items[position], position)
            }
        }
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, position: Int) {
            binding.apply {
                this.vm = viewModel
                this.item = item as RecentSearch
            }
        }
    }

    inner class SearchHeaderViewHolder(private val binding: ItemSearchHeaderBinding) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, position: Int) {
            binding.apply {
                this.vm = viewModel
            }
        }
    }
}