package com.example.trainstationapp.presentation.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState
import com.example.trainstationapp.R
import com.google.android.material.button.MaterialButton

@BindingAdapter("showOnLoadStateLoading")
fun showOnLoadStateLoading(
    view: View,
    loadState: LoadState?,
) {
    view.isVisible = loadState is LoadState.Loading
}

@BindingAdapter("hideOnLoadStateLoading")
fun hideOnLoadStateLoading(
    view: View,
    loadState: LoadState?,
) {
    view.isVisible = loadState !is LoadState.Loading
}

@BindingAdapter("showOnLoadStateNotLoading")
fun showOnLoadStateNotLoading(
    view: View,
    loadState: LoadState?,
) {
    view.isVisible = loadState is LoadState.NotLoading
}

@BindingAdapter("showOnLoadStateError")
fun showOnLoadStateNotError(
    view: View,
    loadState: LoadState?,
) {
    view.isVisible = loadState is LoadState.Error
}

@BindingAdapter("showTextOnLoadStateError")
fun showTextOnLoadStateError(
    textView: TextView,
    loadState: LoadState?,
) {
    if (loadState is LoadState.Error) {
        textView.text = loadState.error.localizedMessage
    }
}

@BindingAdapter("favorite")
fun boolToImageView(
    imageView: ImageView,
    boolean: Boolean?,
) {
    boolean?.let {
        imageView.setImageResource(
            if (it) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
        )
    }
}

@BindingAdapter("favorite")
fun boolToMaterialButton(
    button: MaterialButton,
    boolean: Boolean?,
) {
    boolean?.let {
        button.icon =
            ContextCompat.getDrawable(
                button.context,
                if (it) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
            )
    }
}
