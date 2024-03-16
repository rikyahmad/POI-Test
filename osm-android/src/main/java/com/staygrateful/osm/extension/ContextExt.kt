package com.staygrateful.osm.extension

import android.app.Activity
import android.app.Application
import android.app.Service
import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.isScreenOn() = (getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive

fun Context.grantReadUriPermission(uriString: String) {
    try {
        // ensure custom reminder sounds play well
        grantUriPermission("com.android.systemui", Uri.parse(uriString), Intent.FLAG_GRANT_READ_URI_PERMISSION)
    } catch (ignored: Exception) {
    }
}

fun Context.windowManager(): WindowManager? {
    val objService = getSystemService(Context.WINDOW_SERVICE)
    if (objService is WindowManager) {
        return objService
    }
    return null
}

fun Context.displayManager(): DisplayManager? {
    val objService = getSystemService(Context.DISPLAY_SERVICE)
    if (objService is DisplayManager) {
        return objService
    }
    return null
}

fun Context.isAndroidTV(): Boolean {
    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE)
    if (uiModeManager is UiModeManager) {
        //showToast("Current ui type : ${uiModeManager.currentModeType} | ${Configuration.UI_MODE_TYPE_TELEVISION}")
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }
    return false
}

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun Configuration.isLandscape(): Boolean {
    return this.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun Context.isDarkMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES
}

fun Context.color(@ColorRes colorRes: Int): Int =
        ContextCompat.getColor(this, colorRes)

fun ContextWrapper?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.showToast(message, post, duration)
}

fun Service?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.applicationContext?.showToast(message, post, duration)
}

fun Application?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.applicationContext?.showToast(message, post, duration)
}

fun Fragment?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.context?.showToast(message, post, duration)
}

fun Activity?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.applicationContext?.showToast(message, post, duration)
}

fun View?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    this?.context?.showToast(message, post, duration)
}

fun Context?.showToast(message: String?, post: Boolean = false, duration: Int = Toast.LENGTH_SHORT) {
    if (this != null) {
        if (post) {
            runOnUi{
                Toast.makeText(this, message, duration).show()
            }
            return
        }
        Toast.makeText(this, message, duration).show()
    }
}

fun runOnUi(runnable: Runnable) {
    Handler(Looper.getMainLooper()).post(runnable)
}

fun runOnUi(runnable: Runnable, delayTime: Long) {
    Handler(Looper.getMainLooper()).postDelayed(runnable, delayTime)
}

@ColorInt
fun Context.getColorInt(colorRes: Int) : Int {
    return ContextCompat.getColor(this, colorRes)
}

fun <T: Activity> Context?.startActivity(clazz: Class<T>) {
    if (this is Activity) {
        this.startActivity(Intent(this, clazz))
    }
}