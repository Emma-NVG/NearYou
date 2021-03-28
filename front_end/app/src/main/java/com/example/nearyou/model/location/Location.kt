package com.example.nearyou.model.location


class Location(private val latitude: Float, private val longitude: Float) {
    companion object {
        val manager: LocationManager = LocationManager()
    }
}