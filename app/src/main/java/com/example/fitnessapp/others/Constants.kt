package com.example.fitnessapp.others

import android.graphics.Color

object Constants {
    val RUNNING_DATABASE_NAME = "running_database.db"
    val REQUEST_CODE_FOR_PERMISSON = 101;

    const val START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val STOP_SERVICE = "ACTION_STOP_SERIVICE"

    val NOTIFICATION_CHANNEL_ID = "notificationChannel"
    val NOTIFICATION_CHANNEL_NAME = "notification"
    val NOTIFICATION_ID = 100
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val LOCTION_UPDATE_INTERVAL = 5000
    const val LOCATION_FASTEST_UPDATE = 2000

    const val TIMER_INTERVAL = 50L

    const val POLYLINE_COLOR = Color.RED
    const val POLY_LINE_WIDTH = 8f
    const val MAP_ZOOM = 20f

}