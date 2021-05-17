package com.elenaneacsu.bookfolio.ui.shelves

import com.elenaneacsu.bookfolio.databinding.ShelfLayoutBinding
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 12/05/21
 */
class ShelfViewHolder(
    private val itemBinding: ShelfLayoutBinding,
    private val itemClickListener: OnItemClickListener,
) : BaseViewHolder<Shelf, ShelfLayoutBinding>(itemBinding) {

    override fun bindItem(item: Shelf) {
        itemBinding.shelf = item
        itemBinding.executePendingBindings()

        itemBinding.constraintContainer.setOnOneOffClickListener {
            itemClickListener.onItemClicked(adapterPosition)
        }
    }
}