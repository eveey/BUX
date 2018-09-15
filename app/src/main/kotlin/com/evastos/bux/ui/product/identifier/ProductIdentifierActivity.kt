package com.evastos.bux.ui.product.identifier

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.evastos.bux.R
import com.evastos.bux.R.id.getProductDetailsButton
import com.evastos.bux.R.id.productIdentifierInputEditText
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.databinding.ActivityProductIdentifierBinding
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import com.evastos.bux.ui.util.extensions.debounceClicks
import com.evastos.bux.ui.util.extensions.hideKeyboard
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_product_identifier.getProductDetailsButton
import kotlinx.android.synthetic.main.activity_product_identifier.productIdentifierInputEditText
import kotlinx.android.synthetic.main.activity_product_identifier.productRootView

class ProductIdentifierActivity : BaseActivity(), NetworkConnectivityObserver {

    private lateinit var productIdentifierViewModel: ProductIdentifierViewModel
    private lateinit var binding: ActivityProductIdentifierBinding

    @SuppressLint("RxSubscribeOnError", "RxLeakedSubscription")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_identifier)
        binding.setLifecycleOwner(this)
        productIdentifierViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductIdentifierViewModel::class.java)
        binding.viewModel = productIdentifierViewModel
        binding.isConnectedToNetwork = true
        supportActionBar?.title = getString(R.string.activity_product_identifier_title)

        productIdentifierInputEditText.textChanges()
                .subscribe { charSeq ->
                    binding.isButtonEnabled = !charSeq.isNullOrEmpty()
                    if (!charSeq.isNullOrEmpty()) {
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

        productIdentifierViewModel.productDetailsLiveData.observe(this,
            Observer { productDetails ->
                productDetails?.let {
                    navigateToProductFeed(it)
                }
            })

        productIdentifierViewModel.errorMessageLiveData.observe(this,
            Observer { errorMessage ->
                errorMessage?.let {
                    showSnackbar(productRootView,
                        it,
                        getString(R.string.action_retry)) {
                        productIdentifierViewModel.retryGetProductDetails()
                    }
                }
            })

        productIdentifierViewModel.progressVisibleLiveData.observe(this,
            Observer { isVisible ->
                if (isVisible == true) {
                    hideSnackbar()
                    productIdentifierInputEditText.hideKeyboard()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        productRootView.requestFocus()
        productIdentifierInputEditText.hideKeyboard()
    }

    override fun onNetworkConnectivityAcquired() {
        binding.isConnectedToNetwork = true
    }

    override fun onNetworkConnectivityLost() {
        binding.isConnectedToNetwork = false
    }

    private fun getProductDetails() {
        productIdentifierViewModel.getProductDetails(productIdentifierInputEditText.text.toString())
    }

    private fun navigateToProductFeed(productDetails: ProductDetails) {
        startActivity(ProductFeedActivity.newIntent(this, productDetails))
    }
}
