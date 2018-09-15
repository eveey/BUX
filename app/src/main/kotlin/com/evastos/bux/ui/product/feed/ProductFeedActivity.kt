package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.evastos.bux.R
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.databinding.ActivityProductFeedBinding
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.bux.ui.util.extensions.setGone
import com.evastos.bux.ui.util.extensions.setVisible
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import kotlinx.android.synthetic.main.activity_product_feed.lastUpdateLabelTextView
import kotlinx.android.synthetic.main.activity_product_feed.lastUpdateTexView
import kotlinx.android.synthetic.main.activity_product_feed.productFeedRootView

class ProductFeedActivity : BaseActivity(), NetworkConnectivityObserver {

    companion object {
        private const val EXTRA_PRODUCT_DETAILS = "extraProductDetails"

        fun newIntent(context: Context, productDetails: ProductDetails) =
                Intent(context, ProductFeedActivity::class.java)
                        .apply {
                            putExtra(EXTRA_PRODUCT_DETAILS, productDetails)
                        }
    }

    private lateinit var productFeedViewModel: ProductFeedViewModel
    private lateinit var binding: ActivityProductFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_feed)
        binding.setLifecycleOwner(this)
        productFeedViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductFeedViewModel::class.java)
        binding.viewModel = productFeedViewModel
        binding.isConnectedToNetwork = true

        supportActionBar?.apply {
            title = getString(R.string.activity_product_feed_title)
            setDisplayHomeAsUpEnabled(true)
        }

        productFeedViewModel.exceptionLiveData.observe(this,
            Observer { errorMessage ->
                if (errorMessage != null) {
                    lastUpdateTexView.setVisible()
                    lastUpdateLabelTextView.setVisible()
                    showSnackbar(
                        productFeedRootView,
                        errorMessage, getString(R.string.action_retry)
                    ) {
                        productFeedViewModel.retrySubscribe(getActivityLifecycle())
                    }
                } else {
                    hideSnackbar()
                    lastUpdateTexView.setGone()
                    lastUpdateLabelTextView.setGone()
                }
            })

        val productDetails = intent.getParcelableExtra<ProductDetails>(EXTRA_PRODUCT_DETAILS)
        productFeedViewModel.subscribeToProductFeed(productDetails, getActivityLifecycle())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkConnectivityAcquired() {
        binding.isConnectedToNetwork = true
        hideSnackbar()
        productFeedViewModel.retrySubscribe(getActivityLifecycle())
    }

    override fun onNetworkConnectivityLost() {
        binding.isConnectedToNetwork = false
        lastUpdateTexView.text = productFeedViewModel.lastUpdatedLiveData.value
    }

    private fun getActivityLifecycle(): Lifecycle =
            AndroidLifecycle.ofLifecycleOwnerForeground(application, this)
}
