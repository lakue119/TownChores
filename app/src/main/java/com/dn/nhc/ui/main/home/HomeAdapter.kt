package com.dn.nhc.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemPheedBinding
import com.dn.nhc.remote.model.common.Pheed
import com.dn.nhc.ui.pheed.PheedViewHolder
import com.dn.nhc.ui.pheed.PheedViewModel
import com.lakue.lakue_library.ui.LakueViewHolder

class HomeAdapter(val viewModel: PheedViewModel) : NeighborHoodChoresAdapter<Pheed>(){
    val Tag = HomeAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        DataBindingUtil.inflate<ItemPheedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pheed,
            parent,
            false
        ).let {
            return PheedViewHolder(it, viewModel)
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int = items.count()

}