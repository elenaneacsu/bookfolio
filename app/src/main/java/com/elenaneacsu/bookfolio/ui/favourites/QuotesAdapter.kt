package com.elenaneacsu.bookfolio.ui.favourites

import android.content.Context
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.databinding.FavouriteQuoteLayoutBinding
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

/**
 * Created by Elena Neacsu on 18/06/21
 */
class QuotesAdapter(
    private val context: Context
) : BaseAdapter<Quote, QuotesAdapter.QuoteViewHolder>(context) {

    private val expansionsCollection = ExpansionLayoutCollection()

    class QuoteViewHolder(val itemBinding: FavouriteQuoteLayoutBinding) :
        BaseViewHolder<Quote, FavouriteQuoteLayoutBinding>(itemBinding) {

        override fun bindItem(item: Quote) {
            itemBinding.quote = item
            itemBinding.expansionLayout.collapse(false)
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val quoteItemBinding = FavouriteQuoteLayoutBinding.inflate(layoutInflater, parent, false)

        return QuoteViewHolder(quoteItemBinding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bindItem(_items[position])
        expansionsCollection.add(holder.itemBinding.expansionLayout)
    }
}