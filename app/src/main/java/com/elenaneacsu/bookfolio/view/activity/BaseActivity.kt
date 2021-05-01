package com.elenaneacsu.bookfolio.view.activity

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.elenaneacsu.bookfolio.view.BaseViewDelegate

abstract class BaseActivity(
    @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity(), BaseViewDelegate {

    var apiRequestCompleted = false

}

