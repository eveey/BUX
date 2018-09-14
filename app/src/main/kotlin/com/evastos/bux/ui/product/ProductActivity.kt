package com.evastos.bux.ui.product

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import com.evastos.bux.R
import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.livedata.LiveStatus
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.base.network.connectivity.NetworkConnectivityObserver
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
import kotlinx.android.synthetic.main.activity_product.getProductDetailsButton
import kotlinx.android.synthetic.main.activity_product.networkConnectivityBanner
import kotlinx.android.synthetic.main.activity_product.productIdentifierInputEditText
import kotlinx.android.synthetic.main.activity_product.productRootView
import kotlinx.android.synthetic.main.activity_product.progressBar

class ProductActivity : BaseActivity(), NetworkConnectivityObserver {

    companion object {
        private const val ERROR_DELAY_MILLIS = 400L
    }

    private lateinit var productViewModel: ProductViewModel

    private val handler = Handler()

    @SuppressLint("RxSubscribeOnError", "RxLeakedSubscription")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        supportActionBar?.title = getString(R.string.activity_product_title)

        productViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductViewModel::class.java)

        productViewModel.productLiveData.observe(this,
            Observer { productDetailsResource ->
                when (productDetailsResource?.liveStatus) {
                    LiveStatus.LOADING -> {
                        hideSnackbar()
                        progressBar.setVisible()
                    }
                    LiveStatus.SUCCESS -> {
                        progressBar.setInvisible()
                        navigateToProductFeed(productDetailsResource.productDetails!!)
                    }
                    LiveStatus.ERROR -> {
                        handler.postDelayed({
                            progressBar.setInvisible()
                            showSnackbar(productRootView,
                                getErrorMessage(productDetailsResource.exception),
                                getString(R.string.action_retry)) {
                                productViewModel.retryGetProductDetails()
                            }
                        }, ERROR_DELAY_MILLIS)
                    }
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

        getProductDetailsButton.debounceClicks().subscribe {
            getProductDetails()
        }
    }

    override fun onStart() {
        super.onStart()
        productRootView.requestFocus()
        productIdentifierInputEditText.hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
    }

    override fun onNetworkConnectivityLost() {
        networkConnectivityBanner.setVisible()
    }

    private fun getProductDetails() {
        productIdentifierInputEditText.hideKeyboard()
        productViewModel.getProductDetails(productIdentifierInputEditText.text.toString())
    }

    private fun navigateToProductFeed(productDetails: ProductDetails) {
        startActivity(ProductFeedActivity.newIntent(this, productDetails))
    }

    private fun getErrorMessage(exception: ApiException?): String {
        if (exception is ApiException.AuthException) {
            return exception.errorMessage ?: getString(R.string.api_error_unauthorized)
        }
        if (exception is ApiException.ServerException) {
            return exception.errorMessage ?: getString(R.string.api_error_server_unavailable)
        }
        if (exception is ApiException.NotFoundException) {
            return getString(R.string.api_error_product_not_found)
        }
        if (exception is ApiException.NetworkException) {
            return getString(R.string.api_error_network)
        }
        return getString(R.string.api_error_general)
    }
}
