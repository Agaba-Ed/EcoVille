package com.cleanworld.ecoville.ui.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity.Companion.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient

class LocationFragment : Fragment(),OnMapReadyCallback {

    private val DEFAULT_ZOOM: Float = 15f
    private val mDefaultLocation: LatLng= LatLng(0.0, 0.0)
    private var mLastKnownLocation: Location? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1
    private var mLocationPermissionGranted: Boolean=false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var map:GoogleMap



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Contruct a Places Client
        Places.initialize(activity!!.applicationContext, getString(R.string.google_maps_key))
        placesClient = Places.createClient(activity!!)

        //Construct a Fused Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map= googleMap

        updateLocationUI()
        getDeviceLocation()
    }

    public fun getLocationPermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
        */
        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try{
        if(mLocationPermissionGranted){
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        }else{
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
            mLastKnownLocation= null
            getLocationPermission()
        }
        }catch (e:SecurityException){
            Log.e("Exception: %s", e.message,e)
        }


    }


    @SuppressLint("MissingPermission", "LongLogTag")
    private fun getDeviceLocation() {
        /*
    * Get the best and most recent location of the device, which may be null in rare
    * cases when a location is not available.
    */
        try {
            if (mLocationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.result
                        if (mLastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM
                            ))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    }


