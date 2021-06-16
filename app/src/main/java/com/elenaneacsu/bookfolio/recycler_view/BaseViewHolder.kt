package com.elenaneacsu.bookfolio.recycler_view

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T, VDB : ViewDataBinding>(
    private val itemBinding: VDB,
) : RecyclerView.ViewHolder(itemBinding.root) {

    open fun bindItem(item: T) {
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }
}