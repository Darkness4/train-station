package com.example.trainstationapp.presentation.util

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState

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