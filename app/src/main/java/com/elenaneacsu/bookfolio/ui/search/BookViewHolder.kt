package com.elenaneacsu.bookfolio.ui.search

import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.google_books_api_models.PartialItem
import com.elenaneacsu.bookfolio.models.google_books_api_models.PartialVolumeInfo
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeInfo
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 19/05/21
 */
class BookViewHolder(
    val itemBinding: BookLayoutBinding,
    private val itemClickListener: OnItemClickListener,
) : BaseViewHolder<PartialItem, BookLayoutBinding>(itemBinding) {

    override fun bindItem(item: PartialItem) {
        itemBinding.book = item.volumeInfo
        itemBinding.executePendingBindings()

        itemBinding.constraintContainer.setOnOneOffClickListener {
            itemClickListener.onItemClicked(adapterPosition)
        }
    }
}