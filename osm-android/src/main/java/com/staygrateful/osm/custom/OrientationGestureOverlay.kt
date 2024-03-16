package com.staygrateful.osm.custom

import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class OrientationGestureOverlay(
    private val mapView: MapView,
    private val onRotationListener: ((Float) -> Unit)? = null,
) : Overlay(), OrientationGestureDetector.RotationListener {

    private val mRotationDetector: OrientationGestureDetector = OrientationGestureDetector (this)

    private var timeLastSet = 0L
    private val deltaTime = 25L
    private var currentAngle = 0f

    override fun onRotate(deltaAngle: Float) {
        currentAngle += deltaAngle
        if (System.currentTimeMillis() - deltaTime > timeLastSet) {
            timeLastSet = System.currentTimeMillis()
            setMapOrientation(mapView.mapOrientation + currentAngle)
        }
    }

    private fun setMapOrientation(mapOrientation: Float) {
        mapView.mapOrientation = mapOrientation
        onRotationListener?.invoke(mapOrientation)
    }

    override fun onTouchEvent(event: MotionEvent?, mapView: MapView?): Boolean {
        if (this.isEnabled) {
            mRotationDetector.onTouch(event)
        }
        return super.onTouchEvent(event, mapView)
    }

    override fun onDetach(mapView: MapView?) {
        super.onDetach(mapView)
        //mapView = null
    }
}