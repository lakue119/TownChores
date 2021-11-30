package com.dn.nhc.ui.pheed.upload

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dn.nhc.R
import com.dn.nhc.databinding.ItemCameraBinding
import com.dn.nhc.databinding.ItemCameraUploadBinding
import com.dn.nhc.remote.model.common.Image
import com.lakue.lakue_library.ui.LakueViewHolder

class PheedCameraUploadAdapter(val viewModel: CameraViewModel) : RecyclerView.Adapter<LakueViewHolder>(){

    val TYPE_UPLOAD = 1001
    val TYPE_ITEM = 1002

    val Tag = PheedCameraUploadAdapter::class.java.name

    val items = ArrayList<Any>()

    fun addItem(item: Any){
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun addImageItem(item: ArrayList<Image>){
        this.items.addAll(item)
        notifyDataSetChanged()
    }

    fun addItem(items: ArrayList<Any>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }

//    fun addBitmap(items: ArrayList<Bitmap>){
//        this.items.addAll(items)
//        notifyDataSetChanged()
//    }

    fun getImages(): ArrayList<Bitmap>{
        val imgs = ArrayList<Bitmap>()
        for(image in items){
            if(image is Bitmap){
                imgs.add(image)
            }
        }
        return imgs
    }

    fun removePosition(position: Int){
        this.items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            TYPE_UPLOAD
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        if(viewType == TYPE_UPLOAD){
            DataBindingUtil.inflate<ItemCameraUploadBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_camera_upload,
                parent,
                false
            ).let {
                return CameraUploadViewHolder(it)
            }
        } else {
            DataBindingUtil.inflate<ItemCameraBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_camera,
                parent,
                false
            ).let {
                return CameraViewHolder(it)
            }
        }

    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        if(holder is CameraViewHolder){
            holder.onBind(items[position], position)
        } else if(holder is CameraUploadViewHolder){
            holder.onBind(items[position], position)
        }
    }

    override fun getItemCount(): Int  = items.size

    /**
     * ViewHolder
     */

    inner class CameraUploadViewHolder(private val binding: ItemCameraUploadBinding) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, position: Int) {
            val count = item as Int
            binding.apply {
                this.vm = viewModel
                this.count = items.size-1
            }
        }
    }

    inner class CameraViewHolder(private val binding: ItemCameraBinding) : LakueViewHolder(binding.root) {
        override fun onBind(item: Any, position: Int) {
            when(item){
                is Bitmap -> {
                    binding.apply {
                        this.vm = viewModel
                        this.bitmap = item
                        this.item = null
                        this.pos = position
                    }
                }
                is Image -> {
                    binding.apply {
                        this.vm = viewModel
                        this.bitmap = null
                        this.item = item
                        this.pos = position
                    }
                }
            }
//            val bitmap = item as Bitmap
//            binding.apply {
//                this.vm = viewModel
//                this.bitmap = bitmap
//                this.pos = position
//            }
        }
    }
}