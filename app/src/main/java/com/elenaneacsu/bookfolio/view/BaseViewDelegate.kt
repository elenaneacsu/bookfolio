package com.elenaneacsu.bookfolio.view

interface BaseViewDelegate {
    fun hideProgress()
    fun showProgress()
    fun errorAlert(message: String)
    fun successAlert(message: String)
}