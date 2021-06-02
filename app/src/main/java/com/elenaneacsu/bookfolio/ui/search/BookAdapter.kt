package com.elenaneacsu.bookfolio.ui.search

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.google_books_api_models.PartialItem
import com.elenaneacsu.bookfolio.models.google_books_api_models.PartialVolumeInfo
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.GlideApp


/**
 * Created by Elena Neacsu on 19/05/21
 */
class BookAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener,
) : BaseAdapter<PartialItem, BookViewHolder>(context),
    BaseViewHolder.OnItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val bookItemBinding = BookLayoutBinding.inflate(layoutInflater, parent, false)

        return BookViewHolder(
            bookItemBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bindItem(_items[position])
        if (_items[position].volumeInfo?.imageLinks?.smallThumbnail != null) {
            val image = _items[position].volumeInfo?.imageLinks?.smallThumbnail
            GlideApp.with(context)
                .load(image)
                .apply(RequestOptions())
                .placeholder(R.drawable.ic_bookshelf_filled_24dp)
                .into(holder.itemBinding.bookCover)
            Log.d("TAG", "onBindViewHolder: image $image")
        } else {
            GlideApp.with(context).clear(holder.itemBinding.bookCover)
            holder.itemBinding.bookCover.setImageDrawable(null)
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.book_layout


    override fun onItemClicked(position: Int) {
        itemClickListener.onBookClicked(_items[position])
    }

    interface OnItemClickListener {
        fun onBookClicked(book: PartialItem)
    }
}