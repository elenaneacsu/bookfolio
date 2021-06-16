package com.elenaneacsu.bookfolio.ui.search

import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 19/05/21
 */
class BookViewHolder(
    val itemBinding: BookLayoutBinding,
    private val itemClickListener: OnItemClickListener,
) : BaseViewHolder<Item, BookLayoutBinding>(itemBinding) {

    override fun bindItem(item: Item) {
        itemBinding.book = item.volumeInfo
        itemBinding.executePendingBindings()

        itemBinding.constraintContainer.setOnOneOffClickListener {
            itemClickListener.onItemClicked(adapterPosition)
        }
    }
}