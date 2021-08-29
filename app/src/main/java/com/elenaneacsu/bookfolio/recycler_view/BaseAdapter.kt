package com.elenaneacsu.bookfolio.recycler_view

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(context: Context) :
    RecyclerView.Adapter<VH>() {


    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    protected val _items: MutableList<T> = mutableListOf()

    constructor(context: Context, items: MutableList<T>) : this(context) {
        this._items.addAll(items)
    }

    override fun getItemCount(): Int = _items.size

    operator fun get(position: Int): T? = _items.getOrNull(position)

    fun clear() {
        _items.clear()
    }

    fun removeAt(position: Int) {
        _items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun add(item: T) {
        _items.add(item)
    }

    fun add(item: T, position: Int) {
        _items.add(position, item)
    }

    fun add(items: List<T>) {
        _items.clear()
        _items.addAll(items)
    }
}
