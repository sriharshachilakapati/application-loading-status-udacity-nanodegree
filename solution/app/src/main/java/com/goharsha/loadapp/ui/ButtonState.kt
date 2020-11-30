package com.goharsha.loadapp.ui

import androidx.annotation.StringRes
import com.goharsha.loadapp.R

enum class ButtonState(@StringRes val text: Int) {
    LOADING(R.string.button_loading),
    DOWNLOADING(R.string.downloading),
    DOWNLOAD(R.string.download)
}