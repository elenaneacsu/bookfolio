package com.elenaneacsu.bookfolio.ui.favourites

import com.elenaneacsu.bookfolio.databinding.FavouriteQuoteLayoutBinding
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

class QuoteViewHolder(
    val itemBinding: FavouriteQuoteLayoutBinding,
    private val itemClickListener: OnQuoteDataClickListener
) :
    BaseViewHolder<Quote, FavouriteQuoteLayoutBinding>(itemBinding) {

    override fun bindItem(item: Quote) {
        itemBinding.quote = item
        itemBinding.pageIcon.setOnOneOffClickListener {
            itemClickListener.onPageNumberChanged(adapterPosition)
        }
        itemBinding.executePendingBindings()
    }

    fun expandQuote() {
        itemBinding.expansionLayout.expand(false)
    }

    interface OnQuoteDataClickListener {
        fun onPageNumberChanged(position: Int)
    }
}