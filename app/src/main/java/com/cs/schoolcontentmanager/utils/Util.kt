package com.cs.schoolcontentmanager.utils

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.utils.Constants.CHANNEL_ID
import com.cs.schoolcontentmanager.utils.Constants.CHANNEL_NAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_URI


object Util {

    fun launchFragment(context: Context, fragment: Fragment, fileUri: Uri? = null, stringBundleValue: String? = null) {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager

        if (stringBundleValue != null) {
            val bundle = Bundle()
            bundle.putString(FILE_NAME, stringBundleValue)
            bundle.putString(FILE_URI, fileUri.toString())
            fragment.arguments = bundle
        }

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(android.R.id.content, fragment).addToBackStack(null).commit()
    }

    fun launchDialog(
        binding: ViewBinding,
        isCancelable: Boolean = true,
        function: (binding: ViewBinding) -> Unit
    ) {
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(isCancelable)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        function(binding)

        dialog.show()
        dialog.window?.attributes = lp
    }

    fun notificationBuilder(context: Context, isUploading: Boolean): NotificationCompat.Builder {
        createNotificationChannel(context)
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {

            if (isUploading) {
                setSmallIcon(R.drawable.ic_cloud_upload)
                setContentTitle("Uploading")
                setContentText("Upload in progress")
            } else {
                setSmallIcon(R.drawable.ic_download)
                setContentTitle("Downloading")
                setContentText("Download in progress")
            }
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Suppress("DEPRECATION")
    fun isInternetConnected(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifiConnected: Boolean
        val mobileConnected: Boolean

        val activeInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (activeInfo?.isConnected == true) {
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            wifiConnected = false
            mobileConnected = false
        }

        return wifiConnected || mobileConnected
    }

}