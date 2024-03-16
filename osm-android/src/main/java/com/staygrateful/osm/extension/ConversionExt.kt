package com.staygrateful.osm.extension

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.px(): Float {
    return value * LocalDensity.current.density
}

@Composable
fun Int.pxToDp(): Dp {
    return (this / LocalDensity.current.density).dp
}

@Composable
fun Float.pxToDp(): Dp {
    return (this / LocalDensity.current.density).dp
}

@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

@Composable
fun Int.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}


fun Dp.toPx(): Float {
    return value * Resources.getSystem().displayMetrics.density
}

fun Dp.toPxInt(): Int {
    return toPx().toInt()
}

fun Float.convertToDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}

fun Int.convertToDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}