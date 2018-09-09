package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.evastos.bux.R
import com.evastos.bux.ui.base.BaseActivity

class ProductFeedActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, ProductFeedActivity::class.java)
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
    }
}
