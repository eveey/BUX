package com.evastos.bux.ui.product

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.evastos.bux.R
import com.evastos.bux.ui.base.BaseActivity

class ProductActivity : BaseActivity() {

    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProductViewModel::class.java)
    }
}
