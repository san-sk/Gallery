package com.san.gallery.utils

import android.view.View


fun View?.setVisibility(show: Boolean) {
    this?.visibility = if (show) View.VISIBLE else View.GONE
}