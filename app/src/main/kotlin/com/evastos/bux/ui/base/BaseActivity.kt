package com.evastos.bux.ui.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.evastos.bux.ui.util.hideIfShown
import com.evastos.bux.ui.util.showSnackbarForView
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    protected fun showSnackbar(
        view: View,
        snackbarMessage: String,
        actionMessage: String? = null,
        action: (() -> Unit)? = null) {
        snackbar = showSnackbarForView(view, snackbarMessage, actionMessage, action)
    }

    protected fun hideSnackbar() {
        snackbar.hideIfShown()
    }
}
