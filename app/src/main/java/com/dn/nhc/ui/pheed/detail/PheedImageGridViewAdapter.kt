package com.dn.nhc.ui.pheed.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemPheedImageGridBinding
import com.dn.nhc.remote.model.common.Image
import com.lakue.lakue_library.ui.LakueViewHolder

class PheedImageGridViewAdapter(val viewModel: PheedDetailViewModel) : NeighborHoodChoresAdapter<Image>(){
    val Tag = PheedImageGridViewAdapter::class.java.name

    val MAX_IMAGE_COUNT = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        DataBindingUtil.inflate<ItemPheedImageGridBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pheed_image_grid,
            parent,
            false
        ).let {
            return PheedGridViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return if(items.count() > MAX_IMAGE_COUNT){
            MAX_IMAGE_COUNT
        } else {
            items.count()
        }
    }


    /**
     * ViewHolder
     */
    inner class PheedGridViewHolder(private val binding: ItemPheedImageGridBinding) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, position: Int) {
            binding.apply {
                this.vm = viewModel
                this.image = (item as Image).imageNameUrl
                this.position = position
                this.images = items
                if(position == 3 && items.count() > MAX_IMAGE_COUNT){
                    this.count = items.count() - MAX_IMAGE_COUNT
                } else {
                    this.count = 0
                }
            }
        }
    }
}