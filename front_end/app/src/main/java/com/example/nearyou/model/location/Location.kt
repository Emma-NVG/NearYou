package com.example.nearyou.model.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(val latitude: Double, val longitude: Double) {
    companion object {
        val manager: LocationManager = LocationManager()
    }
}