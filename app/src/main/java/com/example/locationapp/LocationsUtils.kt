package com.example.locationapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import com.example.locationapp.ui.theme.LocationData
import com.example.locationapp.ui.theme.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationsUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient
     = LocationServices.getFusedLocationProviderClient(context)

    @Suppress("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }
        val locationRequest
                = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    fun hasLocationPermission(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        &&
        ContextCompat.checkSelfPermission(
             context,
             Manifest.permission.ACCESS_COARSE_LOCATION
             ) == PackageManager.PERMISSION_GRANTED
    }

    fun reserveGeocoderLocation(location: LocationData): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val addresses : MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
        return if(addresses != null && addresses.isNotEmpty()){
            addresses[0].getAddressLine(0)
        }else{
            "Unknown location"
        }
    }
}