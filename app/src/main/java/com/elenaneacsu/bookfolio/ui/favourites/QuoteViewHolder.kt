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
        itemBinding.apply {
            quote = item

            quoteFull.setOnOneOffClickListener {
                itemClickListener.onTextChanged(adapterPosition)
            }

            pageIcon.setOnOneOffClickListener {
                itemClickListener.onPageNumberChanged(adapterPosition)
            }

            dateIcon.setOnOneOffClickListener {
                itemClickListener.onDateChanged(adapterPosition)
            }
        }
        itemBinding.executePendingBindings()
    }

    interface OnQuoteDataClickListener {
        fun onTextChanged(position: Int)
        fun onPageNumberChanged(position: Int)
        fun onDateChanged(position: Int)
    }
}