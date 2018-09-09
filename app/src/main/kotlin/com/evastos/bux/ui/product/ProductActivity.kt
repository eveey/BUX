package com.evastos.bux.ui.product

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.evastos.bux.R
import com.evastos.bux.ui.base.BaseActivity
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import com.evastos.bux.ui.util.debounceClicks
import kotlinx.android.synthetic.main.activity_product.clickButton

class ProductActivity : BaseActivity() {

    private lateinit var productViewModel: ProductViewModel

    @SuppressLint("RxLeakedSubscription", "RxSubscribeOnError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductViewModel::class.java)

        clickButton.debounceClicks()
                .subscribe {
                    startActivity(Intent(this, ProductFeedActivity::class.java))
                }
    }
}
