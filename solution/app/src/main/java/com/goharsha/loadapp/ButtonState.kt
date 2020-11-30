package com.goharsha.loadapp

import androidx.annotation.StringRes

enum class ButtonState(@StringRes val text: Int) {
    LOADING(R.string.button_loading),
    DOWNLOADING(R.string.downloading),
    DOWNLOAD(R.string.download)
}