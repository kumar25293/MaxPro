package com.maxpro.systemtask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.maxpro.systemtask.R
import com.maxpro.systemtask.customdialog.CustomProgressDialog


object Constants {

    const val BASE_URL = "https://api.stackexchange.com/"
    const val  RETROFIT1 ="retrofit"
    const val  OKHTTP ="okhttp1"

     fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun dismissProgressDialog(progressDialog:CustomProgressDialog){
        Handler(Looper.getMainLooper()).postDelayed({
            // Dismiss progress bar after 4 seconds
            progressDialog.stop()
        }, 1000)
    }

    fun showsProgressDialog(title:String,progressDialog:CustomProgressDialog){
        Handler(Looper.getMainLooper()).post {
            // Dismiss progress bar after 4 seconds
            progressDialog.start(title)
            progressDialog.start()
        }
    }


    fun showErrorDialog(errormsg:String,context: Context){
        val builder = AlertDialog.Builder (context, com.maxpro.systemtask.R.style.CustomAlertDialog)
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.custom_alert_view,null , false)
        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)
        val tvError: TextView = dialogView.findViewById<TextView>(R.id.tv_errormsg)
        tvError.text =errormsg
        builder.setView(dialogView)
        val  alertDialog = builder.create()
        buttonOk.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

}