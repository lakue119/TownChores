package com.dn.nhc.ui.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresAdapter
import com.dn.nhc.databinding.ItemNotificationBinding
import com.dn.nhc.remote.model.common.Pushes
import com.lakue.lakue_library.ui.LakueViewHolder

class NotificationAdapter(val viewModel: NotificationViewModel) : NeighborHoodChoresAdapter<Pushes>() {

    private val Tag = NotificationAdapter::class.java.name
    private val pusherViewHolderMap = HashMap<Int, NotificationViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LakueViewHolder {
        DataBindingUtil.inflate<ItemNotificationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_notification,
            parent,
            false
        ).let {
            return NotificationViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: LakueViewHolder, position: Int) {
        pusherViewHolderMap[position] = holder as NotificationViewHolder
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun readPusher(position: Int){
        pusherViewHolderMap[position]?.readPusher()
    }
    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : LakueViewHolder(binding.root) {

        lateinit var item: Pushes
        override fun onBind(item: Any, pos: Int) {
            this.item = item as Pushes

            binding.apply {
                this.vm = viewModel
                this.item = this@NotificationViewHolder.item
                this.position = pos
            }
        }
        fun readPusher(){
            item.isRead = true
            binding.item = this@NotificationViewHolder.item
        }
    }



}