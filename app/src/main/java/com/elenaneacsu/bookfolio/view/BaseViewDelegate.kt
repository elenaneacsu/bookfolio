package com.elenaneacsu.bookfolio.view

/**
 * Created by Grigore Cristian-Andrei on 12.03.2021.
 */
interface BaseViewDelegate {
    fun hideProgress()
    fun showProgress()
    fun errorAlert(message: String)
    fun successAlert(message: String)
}