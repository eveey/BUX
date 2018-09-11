package com.evastos.bux.ui.product

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.inputmethod.EditorInfo
import com.evastos.bux.R
import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import com.evastos.bux.ui.util.debounceClicks
import com.evastos.bux.ui.util.disable
import com.evastos.bux.ui.util.enable
import com.evastos.bux.ui.util.hideIfShown
import com.evastos.bux.ui.util.showSnackbar
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_product.productIdInputEditText
import kotlinx.android.synthetic.main.activity_product.productRootView
import kotlinx.android.synthetic.main.activity_product.submitButton

class ProductActivity : BaseActivity() {

    private lateinit var productViewModel: ProductViewModel

    private var snackbar: Snackbar? = null

    @SuppressLint("RxLeakedSubscription", "RxSubscribeOnError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        with(supportActionBar) {
            title = getString(R.string.activity_product_title)
        }

        productViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductViewModel::class.java)

        productIdInputEditText.textChanges()
                .subscribe { charSeq ->
                    if (charSeq.isNullOrEmpty()) {
                        submitButton.disable()
                    } else {
                        submitButton.enable()
                    }
                }

        productIdInputEditText.editorActionEvents().subscribe { event ->
            if (event.actionId() == EditorInfo.IME_ACTION_DONE
                    && !productIdInputEditText.text.isNullOrEmpty()) {
                navigateToProductFeed()
            }
        }

        submitButton.debounceClicks().subscribe {
            navigateToProductFeed()
        }
    }

    override fun onStart() {
        super.onStart()
        productRootView.requestFocus()
    }

    private fun navigateToProductFeed() {
        val productIdInputText = productIdInputEditText.text.toString()
        productViewModel.getProductDetails(ProductId(productIdInputText))
        startActivity(ProductFeedActivity.newIntent(this, ProductId(productIdInputText)))
    }

    protected fun showSnackbar(snackbarMessage: String) {
        snackbar = showSnackbar(productRootView, snackbarMessage, getString(R.string.action_retry)) {
            hideSnackbar()
        }
    }

    protected fun hideSnackbar() {
        snackbar.hideIfShown()
    }
}
