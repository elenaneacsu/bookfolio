package com.elenaneacsu.bookfolio.ui.shelves

import android.content.Context
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.ShelfLayoutBinding
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder

/**
 * Created by Elena Neacsu on 12/05/21
 */
class ShelfAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener,
) : BaseAdapter<Shelf, ShelfViewHolder>(context),
    BaseViewHolder.OnItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        val itemPromotionBinding = ShelfLayoutBinding.inflate(layoutInflater, parent, false)

        return ShelfViewHolder(
            itemPromotionBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) =
        holder.bindItem(_items[position])


    override fun onItemClicked(position: Int) {
        itemClickListener.onShelfClicked(_items[position])
    }

    override fun getItemViewType(position: Int): Int = R.layout.shelf_layout

    interface OnItemClickListener {
        fun onShelfClicked(shelf: Shelf)
    }
}