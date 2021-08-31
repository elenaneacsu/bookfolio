package com.elenaneacsu.bookfolio.ui.favourites

import android.view.View
import com.elenaneacsu.bookfolio.R
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

            iconQuote.setImageResource(if (item.isFavourite) R.drawable.ic_favorite_24dp else R.drawable.ic_navigation_quotes_24dp)

            if (item.isCurrentlyDisplayedInFavourites) {
                addToFavourite.visibility = View.GONE
                titleLayout.visibility = View.VISIBLE
                titleSeparator.visibility = View.VISIBLE
                authorLayout.visibility = View.VISIBLE
                authorSeparator.visibility = View.VISIBLE
            } else {
                addToFavourite.visibility = View.VISIBLE
                titleLayout.visibility = View.GONE
                titleSeparator.visibility = View.GONE
                authorLayout.visibility = View.GONE
                authorSeparator.visibility = View.GONE
            }

            quoteFull.setOnOneOffClickListener {
                itemClickListener.onTextChanged(adapterPosition)
            }

            pageIcon.setOnOneOffClickListener {
                itemClickListener.onPageNumberChanged(adapterPosition)
            }

            dateIcon.setOnOneOffClickListener {
                itemClickListener.onDateChanged(adapterPosition)
            }

            addToFavourite.setOnOneOffClickListener {
                itemClickListener.onQuoteAddedToOrRemovedFromFavourites(adapterPosition)
            }

            removeQuote.setOnOneOffClickListener {
                itemClickListener.onQuoteRemoved(adapterPosition)
            }
        }
        itemBinding.executePendingBindings()
    }

    interface OnQuoteDataClickListener {
        fun onTextChanged(position: Int)
        fun onPageNumberChanged(position: Int)
        fun onDateChanged(position: Int)
        fun onQuoteAddedToOrRemovedFromFavourites(position: Int)
        fun onQuoteRemoved(position: Int)
    }
}