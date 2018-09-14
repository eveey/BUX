package com.evastos.bux.ui.databinding

import android.databinding.BindingAdapter
import android.view.View
import com.evastos.bux.ui.util.extensions.disable
import com.evastos.bux.ui.util.extensions.enable
import com.evastos.bux.ui.util.extensions.setGone
import com.evastos.bux.ui.util.extensions.setInvisible
import com.evastos.bux.ui.util.extensions.setVisible

@BindingAdapter("isShown")
fun bindIsShown(view: View, isShown: Boolean) {
    if (isShown) {
        view.setVisible()
    } else {
        view.setGone()
    }
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.setVisible()
    } else {
        view.setInvisible()
    }
}

@BindingAdapter("isEnabled")
fun bindIsEnabled(view: View, isEnabled: Boolean) {
    if (isEnabled) {
        view.enable()
    } else {
        view.disable()
    }
}
