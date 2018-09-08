package com.evastos.bux.ui.product.feed

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.evastos.bux.R
import com.evastos.bux.ui.base.BaseActivity

class ProductFeedActivity : BaseActivity() {

    private lateinit var productFeedViewModel: ProductFeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_feed)

        productFeedViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductFeedViewModel::class.java)
    }
}