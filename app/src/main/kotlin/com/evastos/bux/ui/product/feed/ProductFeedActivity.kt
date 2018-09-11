package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.evastos.bux.R
import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.ui.base.BaseActivity
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle

class ProductFeedActivity : BaseActivity() {

    companion object {
        private const val EXTRA_PRODUCT_ID = "extraProductId"

        fun newIntent(context: Context, productId: ProductId) =
                Intent(context, ProductFeedActivity::class.java)
                        .apply {
                            putExtra(EXTRA_PRODUCT_ID, productId)
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
        val productId = intent.getParcelableExtra<ProductId>(EXTRA_PRODUCT_ID)
        productFeedViewModel.subscribeToProductFeed(productId,
            AndroidLifecycle.ofLifecycleOwnerForeground(application, this))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
