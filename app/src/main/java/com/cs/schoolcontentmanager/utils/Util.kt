package com.cs.schoolcontentmanager.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME

object Util {

    fun launchFragment(context: Context, fragment: Fragment, stringBundleValue: String? = null) {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager

        if (stringBundleValue != null) {
            val bundle = Bundle()
            bundle.putString(FILE_NAME, stringBundleValue)
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
}