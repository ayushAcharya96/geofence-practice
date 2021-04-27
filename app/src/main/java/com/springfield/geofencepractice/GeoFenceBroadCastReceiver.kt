package com.springfield.geofencepractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoFenceBroadCastReceiver : BroadcastReceiver() {

    private val TAG = "GeoFenceBroadCastReceiver"



    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show()

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        Toast.makeText(context, "Receiver GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show()

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofencing event...")
            return
        }

        val notificationHelper = NotificationHelper(context)
        val transitionType = geofencingEvent.geofenceTransition

        val geofenceList = geofencingEvent.triggeringGeofences
        for (geofence in geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.requestId)

            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show()
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER","GEOFENCE_TRANSITION_ENTER", MapsActivity::class.java)
                }
                Geofence.GEOFENCE_TRANSITION_DWELL -> {
                    Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show()
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL","GEOFENCE_TRANSITION_DWELL", MapsActivity::class.java)
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show()
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT","GEOFENCE_TRANSITION_EXIT", MapsActivity::class.java)

                }
            }
        }
    }
}