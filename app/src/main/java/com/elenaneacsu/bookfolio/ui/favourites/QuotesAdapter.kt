package com.elenaneacsu.bookfolio.ui.favourites

import android.content.Context
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.databinding.FavouriteQuoteLayoutBinding
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

/**
 * Created by Elena Neacsu on 18/06/21
 */
class QuotesAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter<Quote, QuoteViewHolder>(context),
    QuoteViewHolder.OnQuoteDataClickListener {

    private val expansionsCollection = ExpansionLayoutCollection()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val quoteItemBinding = FavouriteQuoteLayoutBinding.inflate(layoutInflater, parent, false)

        return QuoteViewHolder(quoteItemBinding, this)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bindItem(_items[position])
        expansionsCollection.add(holder.itemBinding.expansionLayout)
    }

    override fun onTextChanged(position: Int) {
        val quote = _items[position]
        itemClickListener.onQuoteFullTextClicked(quote)
    }

    override fun onPageNumberChanged(position: Int) {
        val quote = _items[position]
        itemClickListener.onPageIconClicked(quote)
    }

    override fun onDateChanged(position: Int) {
        val quote = _items[position]
        itemClickListener.onDateIconClicked(quote)
    }

    fun addQuote(quote: Quote) {
        add(quote)
        notifyItemInserted(_items.size - 1)
    }

    fun updateQuote(quote: Quote, updatedQuote: Quote) {
        _items[_items.indexOf(quote)] = updatedQuote
        //the date is updated
        if (quote.date != updatedQuote.date) {
            _items.sortBy { it.date }
            notifyDataSetChanged()
        } else {
            //other quote data is updated
            notifyItemChanged(_items.indexOf(updatedQuote))
            notifyItemRangeChanged(0, _items.size)
        }
    }

    interface OnItemClickListener {
        fun onQuoteFullTextClicked(quote: Quote)
        fun onPageIconClicked(quote: Quote)
        fun onDateIconClicked(quote: Quote)
    }
}