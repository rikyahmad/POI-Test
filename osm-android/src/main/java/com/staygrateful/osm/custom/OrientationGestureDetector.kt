package com.staygrateful.osm.custom

import android.view.MotionEvent
import kotlin.math.atan2


class OrientationGestureDetector(private val mListener: RotationListener) {
    interface RotationListener {
        fun onRotate(deltaAngle: Float)
    }

    private var mRotation = 0f

    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(deltaY, deltaX)
        return Math.toDegrees(radians).toFloat()
    }

    fun onTouch(e: MotionEvent?) {
        if(e == null) return
        if (e.pointerCount != 2) return
        if (e.actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            mRotation = rotation(e)
        }
        val rotation = rotation(e)
        val delta = rotation - mRotation
        mRotation += delta
        mListener.onRotate(delta)
    }
}