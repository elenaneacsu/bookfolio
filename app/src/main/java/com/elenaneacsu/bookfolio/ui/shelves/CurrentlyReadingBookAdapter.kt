package com.elenaneacsu.bookfolio.ui.shelves

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.CurrentlyReadingLayoutBinding
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.GlideApp
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 17/06/21
 */
class CurrentlyReadingBookAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter<BookDetailsMapper, CurrentlyReadingBookAdapter.CurrentlyReadingBookViewHolder>(
    context
),
    BaseViewHolder.OnItemClickListener {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentlyReadingBookViewHolder {
        val bookItemBinding = CurrentlyReadingLayoutBinding.inflate(layoutInflater, parent, false)

        return CurrentlyReadingBookViewHolder(
            bookItemBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: CurrentlyReadingBookViewHolder, position: Int) {
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

    interface OnItemClickListener {
        fun onBookClicked(book: BookDetailsMapper)
    }

    class CurrentlyReadingBookViewHolder(
        val itemBinding: CurrentlyReadingLayoutBinding,
        private val itemClickListener: OnItemClickListener,
    ) : BaseViewHolder<BookDetailsMapper, CurrentlyReadingLayoutBinding>(itemBinding) {

        override fun bindItem(item: BookDetailsMapper) {
            itemBinding.book = item
            itemBinding.executePendingBindings()

            itemBinding.constraintContainer.setOnOneOffClickListener {
                itemClickListener.onItemClicked(adapterPosition)
            }
        }
    }
}