package com.hansung.petlifetimecare.mapPackage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline


class ScreenOffBroadcastReceiver(private val map: GoogleMap, private val polyline: Polyline) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            polyline.remove()
        }
    }
}
