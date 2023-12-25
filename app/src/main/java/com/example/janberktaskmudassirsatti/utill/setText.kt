package com.example.janberktaskmudassirsatti.utill

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.janberktaskmudassirsatti.R
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("android:text")
fun setText(view: MaterialTextView, text: CharSequence?) {
    view.text = text
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}


@BindingAdapter("imageUrl")
fun loadImage(view: AppCompatImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(view.context)
            .load(it)
            .placeholder(R.drawable.imageplace)
            .error(R.mipmap.ic_launcher)
            .into(view)
    }
}
