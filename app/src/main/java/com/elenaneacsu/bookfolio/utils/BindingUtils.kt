package com.elenaneacsu.bookfolio.utils

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.elenaneacsu.bookfolio.R

/**
 * Created by Elena Neacsu on 16/06/21
 */
@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context).load(url).into(this)
    } else {
        Glide.with(context).load(context.getDrawable(R.drawable.ic_bookshelf_filled_24dp)).into(this)
    }
}

@BindingAdapter("htmlText")
fun TextView.formatHtmlText(text: String?) {
    if(!text.isNullOrEmpty()) {
        setText(Html.fromHtml(text).toString())
    } else {
        setText(context.getString(R.string.no_description_available))
    }
}