package com.elenaneacsu.bookfolio.ui.search

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.utils.GlideApp


/**
 * Created by Elena Neacsu on 19/05/21
 */
class BookAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter<BookDetailsMapper, BookViewHolder>(context),
    BookViewHolder.OnBookItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val bookItemBinding = BookLayoutBinding.inflate(layoutInflater, parent, false)

        return BookViewHolder(
            bookItemBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bindItem(_items[position])
        if (_items[position].getSmallCover() != null) {
            val image = _items[position].getSmallCover()
            GlideApp.with(context)
                .load(image)
                .apply(RequestOptions())
                .placeholder(R.drawable.ic_bookshelf_filled_24dp)
                .into(holder.itemBinding.bookCover)
        } else {
            GlideApp.with(context).clear(holder.itemBinding.bookCover)
            holder.itemBinding.bookCover.setImageDrawable(null)
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.book_layout


    override fun onItemClicked(position: Int) {
        itemClickListener.onBookClicked(_items[position])
    }

    override fun onRemoveBook(position: Int) {
        itemClickListener.onRemoveBook(_items[position])
    }

    fun onBookRemoved(book: BookDetailsMapper) {
        val position = _items.indexOf(book)
        _items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, _items.size)
    }

    interface OnItemClickListener {
        fun onBookClicked(book: BookDetailsMapper)
        fun onRemoveBook(book: BookDetailsMapper) {}
    }
}