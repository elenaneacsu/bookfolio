package com.elenaneacsu.bookfolio.extensions

import android.Manifest
import android.content.Context
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File

/**
 * Created by Elena Neacsu on 28/08/2021
 */
interface PermissionsCallback {
    // Pass request granted status i.e true or false
    fun onPermissionRequest(granted: Boolean)
}

fun Context.requestCameraPermission(callback: PermissionsCallback) {
    requestSinglePermission(Manifest.permission.CAMERA, callback)
}

fun Context.requestSinglePermission(permission: String, callback: PermissionsCallback) {
    Dexter.withContext(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                // User has granted the permission
                callback.onPermissionRequest(granted = true)
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?,
            ) {
                // User previously denied the permission, request them again
                token?.continuePermissionRequest()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                // User has denied the permission
                callback.onPermissionRequest(granted = false)
            }
        })
        .check()
}

fun Context.createImageFile(imageFileName: String): File? {
    return try {
        val parent = File(cacheDir, "images")
        parent.mkdirs()
        val file = File(parent, imageFileName)
        Log.d("IMAGE", "createImageFile - ${file.absolutePath}")
        if (file.exists())
            file.delete()
        file
    } catch (e: Exception) {
        null
    }
}