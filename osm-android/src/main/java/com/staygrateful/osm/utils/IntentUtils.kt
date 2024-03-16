package com.staygrateful.osm.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder


object IntentUtils {

    fun launchGoogleMapsApp(context: Context, lat: Double, lon: Double) {
        val appPackage = if (isAppInstalled(context, "com.google.android.apps.maps")) {
            "com.google.android.apps.maps"
        }else {
            null
        }
        if (appPackage == null) {
            Toast.makeText(context, "Google maps is not installed", Toast.LENGTH_LONG).show()
            return
        }
        try {
            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                val url = "http://maps.google.com/maps?daddr=$lat,$lon"
                setPackage(appPackage)
                data = Uri.parse(url)
            }
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Cannot open google maps", Toast.LENGTH_LONG).show()
        }
    }

    fun launchWhatsappApp(context: Context, phone: String, message: String) {
        val appPackage = if (isAppInstalled(context, "com.whatsapp")) {
            "com.whatsapp"
        } else if (isAppInstalled(context, "com.whatsapp.w4b")) {
            "com.whatsapp.w4b"
        } else {
            null
        }
        if (appPackage == null) {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
            return
        }
        val sendIntent = try {
            Intent(Intent.ACTION_VIEW).apply {
                val url = "https://api.whatsapp.com/send?phone=$phone&text=${
                    URLEncoder.encode(message, "UTF-8")
                }"
                setPackage(appPackage)
                data = Uri.parse(url)
            }
        } catch (e: Exception) {
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                setPackage(appPackage)
            }
        }
        context.startActivity(sendIntent)
    }

    fun isAppInstalled(ctx: Context, packageName: String): Boolean {
        val pm = ctx.packageManager
        return try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}