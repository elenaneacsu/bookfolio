package com.elenaneacsu.bookfolio.ui.search.book_details

import android.content.Context
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.databinding.BottomSheetOptionItemBinding
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.recycler_view.BaseAdapter
import com.elenaneacsu.bookfolio.recycler_view.BaseViewHolder
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener

/**
 * Created by Elena Neacsu on 17/06/21
 */
class ShelfOptionsAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter<Shelf, ShelfOptionsAdapter.ShelfOptionViewHolder>(context),
    BaseViewHolder.OnItemClickListener {

    class ShelfOptionViewHolder(
        val itemBinding: BottomSheetOptionItemBinding,
        private val itemClickListener: OnItemClickListener
    ) : BaseViewHolder<Shelf, BottomSheetOptionItemBinding>(itemBinding) {

        override fun bindItem(item: Shelf) {
            itemBinding.shelf = item
            itemBinding.executePendingBindings()

            itemBinding.optionLayoutContainer.setOnOneOffClickListener {
                itemClickListener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfOptionViewHolder {
        val shelfOptionItemBinding =
            BottomSheetOptionItemBinding.inflate(layoutInflater, parent, false)

        return ShelfOptionViewHolder(
            shelfOptionItemBinding,
            this
        )
    }

    override fun onBindViewHolder(holder: ShelfOptionViewHolder, position: Int) {
        holder.bindItem(_items[position])
    }

    override fun onItemClicked(position: Int) {
        itemClickListener.onShelfOptionClicked(_items[position])
    }

    interface OnItemClickListener {
        fun onShelfOptionClicked(shelf: Shelf)
    }
}