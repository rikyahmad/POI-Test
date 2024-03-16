package com.staygrateful.osm.compose

internal interface OsmAndNode {
    fun onAttached() {}
    fun onRemoved() {}
    fun onCleared() {}
}

internal object OsmNodeRoot : OsmAndNode