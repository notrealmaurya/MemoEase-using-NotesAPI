package com.maurya.memoease.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun saveBitmapAsImage(bitmap: Bitmap, context: Context) {
    val storageDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val timeStamp: String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "NoteImage_$timeStamp.png"
    val file = File(storageDir, fileName)
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    } catch (e: IOException) {
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
    }

    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    mediaScanIntent.data = Uri.fromFile(file)
    context.sendBroadcast(mediaScanIntent)
}

@SuppressLint("ObsoleteSdkInt")
fun checkInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false

        return networkInfo.isConnected
    }
}

fun formatDate(rawDate: String): String {
    // Parse the raw date using SimpleDateFormat
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

    try {
        val date = parser.parse(rawDate)
        return formatter.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return rawDate // Return the raw date in case of parsing error
    }
}

fun showConfirmationDialog(
    context: Context,
    title: String,
    message: String,
    positiveButtonAction: () -> Unit
) {
    val alertDialogBuilder = MaterialAlertDialogBuilder(context)
    alertDialogBuilder.setTitle(title)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton("Yes") { _, _ -> positiveButtonAction() }
    alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
    alertDialogBuilder.create().show()
}


fun showToast(context: Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard(view: View){
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }catch (e: Exception){

    }
}