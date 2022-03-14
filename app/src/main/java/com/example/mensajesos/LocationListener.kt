package com.example.mensajesos

import android.location.Location
import android.location.LocationListener

class locationListener:LocationListener {
    override fun onLocationChanged(p0: Location) {
        var latitude:String = "Mi latitud es: "+p0.latitude
        var lengitude:String = "Mi longitud es: "+p0.longitude
    }

}