package com.elenaneacsu.bookfolio.ui.search

import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 19/05/21
 */
class BookViewHolder(
    val itemBinding: BookLayoutBinding,
    private val itemClickListener: OnBookItemClickListener,
) : BaseViewHolder<BookDetailsMapper, BookLayoutBinding>(itemBinding) {

    override fun bindItem(item: BookDetailsMapper) {
        itemBinding.book = item
        itemBinding.executePendingBindings()

        itemBinding.constraintContainer.setOnOneOffClickListener {
            itemClickListener.onItemClicked(adapterPosition)
        }
        itemBinding.removeBook.setOnOneOffClickListener {

        }
    }

    interface OnBookItemClickListener : OnItemClickListener {
        fun onRemoveBook(position: Int)
    }
}