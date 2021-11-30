package com.lakue.lakue_library.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class LakueAdapter<T> : RecyclerView.Adapter<LakueViewHolder>(){

    val items = ArrayList<T>()

    fun addItem(item: T){
        val startCount = this.items.size

        this.items.add(item)
        notifyItemRangeInserted(startCount, startCount+1)
    }

    fun addItem(items: List<T>){
        val startCount = this.items.size
        val endCount = startCount + items.size

        this.items.addAll(items)
        notifyItemRangeInserted(startCount, endCount)
    }

    fun clear(){
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}