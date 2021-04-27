package com.springfield.geofencepractice

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "MapsActivity"

    private lateinit var mMap: GoogleMap

    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 2090
    private val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 2091

    lateinit var geofencingClient: GeofencingClient
    lateinit var geoFenceHelper: GeoFenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geofencingClient = LocationServices.getGeofencingClient(this)
        geoFenceHelper = GeoFenceHelper(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

//        val fences = mutableListOf<LatLng>()
//        fences.add(LatLng(27.6869, 85.3151))
//        fences.add(LatLng(27.6846, 85.2997))
//        fences.add(LatLng(27.686744906070317,85.31232342123985))
//        fences.add(LatLng(27.688288115833267,85.3092247992754))
//        fences.add(LatLng(27.689285341937723,85.28414212167263))
//        fences.add(LatLng(27.716707715822647,85.28358053416014))
//        fences.add(LatLng(27.727027464934533,85.3045778721571))
//        fences.add(LatLng(27.735280542945993,85.30600983649492))
//        fences.add(LatLng(27.7349998120492,85.31442694365977))
//        fences.add(LatLng(27.735193593601785,85.3182977065444))
//        fences.add(LatLng(27.739117224964858,85.32070063054562))
//        fences.add(LatLng(27.72143840222945,85.31323101371527))
////
////
//        for (fence in fences) {
//            mMap.addMarker(MarkerOptions().position(fence).title("Marker in Sydney"))
//            tryAddingFence(fence)
//        }

        val ebPearls = LatLng(27.7393, 85.3213)

//        mMap.addMarker(MarkerOptions().position(ebPearls).title("Marker in Sydney"))
        tryAddingFence(ebPearls)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ebPearls, 16f))
        enableUserLocation()

        mMap.setOnMapClickListener {
            if (Build.VERSION.SDK_INT >= 29) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.d(TAG, it.toString())

                    tryAddingFence(it)
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACKGROUND_LOCATION_ACCESS_REQUEST_CODE)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACKGROUND_LOCATION_ACCESS_REQUEST_CODE)
                    }
                }
            } else {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                tryAddingFence(it)
            }
        }
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    mMap.isMyLocationEnabled = true
                    return
                }

            } else {
                Toast.makeText(this, "Fine location access is necessary for geofences to trigger...", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "you can add geofences...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Background location access is necessary for geofences to trigger...", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun tryAddingFence(latLng: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng))
        mMap.addCircle(
                CircleOptions()
                        .center(latLng)
                        .radius(300.0)
                        .strokeColor(Color.argb(255, 0, 0, 255))
                        .fillColor(Color.argb(100, 0, 0, 255))
        )
        addGeofence(latLng,300.0 )
    }

    private fun addGeofence(latLng: LatLng, radius: Double) {

        val geofence = geoFenceHelper.getGeoFence(
                "geo_fence",
                latLng,
                radius,
                Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        val geofenceRequest = geoFenceHelper.getGeoFencingRequest(geofence)
        val pendingIntent = geoFenceHelper.getPendingIntent()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(geofenceRequest, pendingIntent)
                .addOnSuccessListener {
                    Toast.makeText(this, "Geofence Added...", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onSuccess: Geofence Added...")
                }
                .addOnFailureListener {
                    val errorMessage = geoFenceHelper.getErrorString(it)
                    Toast.makeText(this, "Geofence onFailure...", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFailure: " + errorMessage)
                }

    }

}