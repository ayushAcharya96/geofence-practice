package com.springfield.geofencepractice

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

class GeoFenceHelper(base: Context?) : ContextWrapper(base) {

    private var pendingIntent: PendingIntent? = null

    fun getGeoFencingRequest(geoFence: Geofence): GeofencingRequest {
        return GeofencingRequest
                .Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geoFence)
                .build()
    }

    fun getGeoFence(id: String, latLng: LatLng, radius: Double, transitionTypes: Int): Geofence {
        return Geofence
                .Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius.toFloat())
                .setRequestId(id)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(300000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()
    }

    @JvmName("getPendingIntent1")
    fun getPendingIntent(): PendingIntent? {
        if(pendingIntent != null) {
            return pendingIntent
        } else {
            val intent = Intent(this, GeoFenceBroadCastReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(this, 1909, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            return pendingIntent
        }
    }

    fun getErrorString(e: Exception): String? {
        if (e is ApiException) {
            when(e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->
                    return "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
                    return "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }
}