package com.dn.nhc.ui.pheed

import com.dn.nhc.databinding.ItemPheedBinding
import com.dn.nhc.remote.model.common.Pheed
import com.lakue.lakue_library.ui.LakueViewHolder

class PheedViewHolder(private val binding: ItemPheedBinding,
                      private val viewModel: PheedViewModel) : LakueViewHolder(binding.root) {
    override fun onBind(item: Any, pos: Int) {
        binding.apply {
            this.vm = viewModel
            this.item = item as Pheed
        }
    }
}