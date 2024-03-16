package com.staygrateful.osm.extension

import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.ui.unit.Dp

fun Number.sp(): Float {
    return try {
        val metrics = Resources.getSystem().displayMetrics
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            metrics
        )
    } catch (e: Exception) {
        0f
    }
}

fun Number.dp(): Float {
    return try {
        val metrics = Resources.getSystem().displayMetrics
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            metrics
        )
    } catch (e: Exception) {
        0f
    }
}

fun Number.px(): Float {
    return try {
        val metrics = Resources.getSystem().displayMetrics
        this.toInt() / metrics.density
    } catch (e: Exception) {
        0f
    }
}

val screenHeight: Int get() = try {
    val metrics = Resources.getSystem().displayMetrics
    metrics.heightPixels
} catch (e: Exception) {
    0
}

val screenWidth: Int get() = try {
    val metrics = Resources.getSystem().displayMetrics
    metrics.widthPixels
} catch (e: Exception) {
    0
}

fun scalePixel(size: Int, dp: Dp): Float {
    val toSize = size + (dp.toPx() * 2).toInt()
    return (toSize / size.toFloat())
}