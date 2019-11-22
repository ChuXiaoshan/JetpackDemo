package com.cxsplay.jpdemo.paging.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cxsplay.jpdemo.R

/**
 * Created by chuxiaoshan on 2019/11/22 10:34.
 *
 */
class CustomAdapter : PagedListAdapter<DataBean, CustomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder = CustomViewHolder(parent)


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataBean>() {
            override fun areItemsTheSame(oldItem: DataBean, newItem: DataBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataBean, newItem: DataBean): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class CustomViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_demo, parent, false)) {
    private val nameView = itemView.findViewById<TextView>(R.id.tv_name)
    var item: DataBean? = null

    fun bindTo(item: DataBean?) {
        this.item = item
        nameView.text = item?.name
    }
}