package com.elenaneacsu.bookfolio.ui.shelves.shelf

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.BookLayoutBinding
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.ui.search.BookViewHolder
import com.elenaneacsu.bookfolio.utils.GlideApp

/**
 * Created by Elena Neacsu on 07/08/2021
 */
class UserBookAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter<UserBook, BookViewHolder>(context),
    BaseViewHolder.OnItemClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val bookItemBinding = BookLayoutBinding.inflate(layoutInflater, parent, false)

        return BookViewHolder(
            bookItemBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        _items[position].item?.let { bookItem ->
            holder.bindItem(bookItem)
            if (bookItem.volumeInfo?.imageLinks?.smallThumbnail != null) {
                val image = bookItem.volumeInfo.imageLinks.smallThumbnail
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
    }

    override fun getItemViewType(position: Int): Int = R.layout.book_layout

    override fun onItemClicked(position: Int) {
        itemClickListener.onUserBookClicked(_items[position])
    }

    interface OnItemClickListener {
        fun onUserBookClicked(userBook: UserBook)
    }
}