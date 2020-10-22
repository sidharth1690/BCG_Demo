package com.skills.bcg_demo.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import android.widget.Toast
import com.skills.bcg_demo.R
import java.util.regex.Pattern

object AppUtils {

    // Common method for showing toast
    fun showToastWithLengthShort(message: String, mContext: Context) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


    // Returns boolean value for valid/invalid email Address format
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // Returns boolean value if device has active internet connection or not
    @Suppress("DEPRECATION")
    fun isNetworkConnected(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
            else -> {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
        }
        return result
    }

    /**
     * alert builder instance to show alert dialog
     */
    fun alertBuilder(message:String,context: Context) :AlertDialog.Builder{
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        return builder;
    }

    /**
     * show toast
     * context: the calling context
     */
    fun showToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


}