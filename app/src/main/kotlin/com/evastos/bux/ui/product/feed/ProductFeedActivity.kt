package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.evastos.bux.R
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.base.network.connectivity.NetworkConnectivityObserver
import com.evastos.bux.ui.util.extensions.setGone
import com.evastos.bux.ui.util.extensions.setVisible
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import kotlinx.android.synthetic.main.activity_product_feed.currentPriceTextView
import kotlinx.android.synthetic.main.activity_product_feed.lastUpdateLabelTextView
import kotlinx.android.synthetic.main.activity_product_feed.lastUpdateTexView
import kotlinx.android.synthetic.main.activity_product_feed.networkConnectivityBanner
import kotlinx.android.synthetic.main.activity_product_feed.previousDayClosingPriceTextView
import kotlinx.android.synthetic.main.activity_product_feed.priceDifferenceTextView
import kotlinx.android.synthetic.main.activity_product_feed.productFeedRootView
import kotlinx.android.synthetic.main.activity_product_feed.tradingProductTextView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_feed)
        supportActionBar?.apply {
            title = getString(R.string.activity_product_feed_title)
            setDisplayHomeAsUpEnabled(true)
        }

        productFeedViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductFeedViewModel::class.java)

        productFeedViewModel.tradingProductNameLiveData
                .observe(this,
                    Observer { tradingProductName ->
                        tradingProductName?.let {
                            tradingProductTextView.text = it
                        }
                    })

        productFeedViewModel.previousDayClosingPriceLiveData.observe(this,
            Observer { previousDayClosingPrice ->
                previousDayClosingPrice?.let {
                    previousDayClosingPriceTextView.text = it
                }
            })

        productFeedViewModel.currentPriceLiveData.observe(this,
            Observer { currentPrice ->
                currentPrice?.let {
                    currentPriceTextView.text = it
                }
            })

        productFeedViewModel.priceDifferenceLiveData.observe(this, Observer { priceDifference ->
            priceDifferenceTextView.text = priceDifference
        })

        productFeedViewModel.productFeedExceptionLiveData.observe(this,
            Observer { exception ->
                exception?.let {
                    showSnackbar(productFeedRootView, getErrorMessage(it), getString(R.string.action_retry)) {
                        productFeedViewModel.retrySubscribeToProductFeed()
                    }
                }
            })

        val productDetails = intent.getParcelableExtra<ProductDetails>(EXTRA_PRODUCT_DETAILS)
        productFeedViewModel.subscribeToProductFeed(productDetails,
            AndroidLifecycle.ofLifecycleOwnerForeground(application, this))

        productFeedViewModel.networkConnectivityLiveData.observe(this,
            Observer { isConnected ->
                if (isConnected == true) {
                    networkConnectivityBanner.setVisible()
                } else {
                    networkConnectivityBanner.setGone()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkConnectivityAcquired() {
        networkConnectivityBanner.setGone()
        lastUpdateLabelTextView.setGone()
        lastUpdateTexView.setGone()
        productFeedViewModel.retrySubscribeToProductFeed()
    }

    override fun onNetworkConnectivityLost() {
        lastUpdateTexView.text = productFeedViewModel.lastUpdatedLiveData.value
        networkConnectivityBanner.setVisible()
        lastUpdateLabelTextView.setVisible()
        lastUpdateTexView.setVisible()
    }

    private fun getErrorMessage(exception: RtfException?): String {
        if (exception is RtfException.NotConnectedException) {
            return getString(R.string.rtf_error_not_connected)
        }
        if (exception is RtfException.NotSubscribedException) {
            return getString(R.string.rtf_error_not_subscribed)
        }
        if (exception is RtfException.NetworkException) {
            return getString(R.string.rtf_error_network)
        }
        return getString(R.string.rtf_error_general)
    }
}
