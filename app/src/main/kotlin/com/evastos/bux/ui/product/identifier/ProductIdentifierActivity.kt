package com.evastos.bux.ui.product.identifier

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.evastos.bux.R
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.network.connectivity.NetworkConnectivityObserver
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import com.evastos.bux.ui.util.extensions.debounceClicks
import com.evastos.bux.ui.util.extensions.disable
import com.evastos.bux.ui.util.extensions.enable
import com.evastos.bux.ui.util.extensions.hideKeyboard
import com.evastos.bux.ui.util.extensions.setGone
import com.evastos.bux.ui.util.extensions.setInvisible
import com.evastos.bux.ui.util.extensions.setVisible
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_product_identifier.getProductDetailsButton
import kotlinx.android.synthetic.main.activity_product_identifier.networkConnectivityBanner
import kotlinx.android.synthetic.main.activity_product_identifier.productIdentifierInputEditText
import kotlinx.android.synthetic.main.activity_product_identifier.productRootView
import kotlinx.android.synthetic.main.activity_product_identifier.progressBar

class ProductIdentifierActivity : BaseActivity(), NetworkConnectivityObserver {

    private lateinit var productIdentifierViewModel: ProductIdentifierViewModel

    @SuppressLint("RxSubscribeOnError", "RxLeakedSubscription")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_identifier)
        supportActionBar?.title = getString(R.string.activity_product_identifier_title)

        productIdentifierViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductIdentifierViewModel::class.java)

        productIdentifierViewModel.productDetailsLiveData.observe(this,
            Observer { productDetails ->
                navigateToProductFeed(productDetails!!)
            })

        productIdentifierViewModel.errorMessageLiveData.observe(this,
            Observer { errorMessage ->
                showSnackbar(productRootView,
                    errorMessage!!,
                    getString(R.string.action_retry)) {
                    productIdentifierViewModel.retryGetProductDetails()
                }
            })

        productIdentifierViewModel.progressShowLiveData.observe(this,
            Observer { showProgressBar ->
                if (showProgressBar == true) {
                    progressBar.setVisible()
                } else {
                    progressBar.setInvisible()
                }
            })

        productIdentifierInputEditText.textChanges()
                .subscribe { charSeq ->
                    if (charSeq.isNullOrEmpty()) {
                        getProductDetailsButton.disable()
                    } else {
                        getProductDetailsButton.enable()
                        hideSnackbar()
                    }
                }

        productIdentifierInputEditText.editorActionEvents()
                .subscribe { event ->
                    if (event.actionId() == EditorInfo.IME_ACTION_DONE
                            && !productIdentifierInputEditText.text.isNullOrEmpty()) {
                        getProductDetails()
                    }
                }

        getProductDetailsButton.debounceClicks()
                .subscribe {
                    getProductDetails()
                }
    }

    override fun onResume() {
        super.onResume()
        productRootView.requestFocus()
        productIdentifierInputEditText.hideKeyboard()
    }

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
    }

    override fun onNetworkConnectivityLost() {
        networkConnectivityBanner.setVisible()
    }

    private fun getProductDetails() {
        hideSnackbar()
        productIdentifierInputEditText.hideKeyboard()
        productIdentifierViewModel.getProductDetails(productIdentifierInputEditText.text.toString())
    }

    private fun navigateToProductFeed(productDetails: ProductDetails) {
        startActivity(ProductFeedActivity.newIntent(this, productDetails))
    }
}
