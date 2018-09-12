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
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import kotlinx.android.synthetic.main.activity_product_feed.currentPriceTextView
import kotlinx.android.synthetic.main.activity_product_feed.previousDayClosingPriceTextView
import kotlinx.android.synthetic.main.activity_product_feed.productFeedRootView
import kotlinx.android.synthetic.main.activity_product_feed.tradingProductTextView

class ProductFeedActivity : BaseActivity() {

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

        productFeedViewModel.tradingProductLiveData
                .observe(this, Observer { productDetails ->
                    productDetails?.let {
                        tradingProductTextView.text = productDetails.displayName
                        previousDayClosingPriceTextView.text = productDetails.closingPrice?.amount.toString()
                        currentPriceTextView.text = productDetails.currentPrice?.amount.toString()
                    }
                })

        productFeedViewModel.productFeedUpdateLiveData.observe(this, Observer { updateEventBody ->
            updateEventBody?.let {
                currentPriceTextView.text = it.currentPrice.toString()
            }
        })

        productFeedViewModel.productFeedExceptionLiveData.observe(this, Observer { exception ->
            exception?.let {
                showSnackbar(productFeedRootView, getErrorMessage(it), getString(R.string.action_retry)) {
                    productFeedViewModel.retrySubscribe()
                }
            }
        })

        val productDetails = intent.getParcelableExtra<ProductDetails>(EXTRA_PRODUCT_DETAILS)
        productFeedViewModel.subscribeToProductFeed(productDetails,
            AndroidLifecycle.ofLifecycleOwnerForeground(application, this))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
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
