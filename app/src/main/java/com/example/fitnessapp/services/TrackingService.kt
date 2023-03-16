package com.example.fitnessapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Insets.add
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import android.view.Gravity.apply
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat.apply
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.others.Constants
import com.example.fitnessapp.others.Constants.LOCATION_FASTEST_UPDATE
import com.example.fitnessapp.others.Constants.LOCTION_UPDATE_INTERVAL
import com.example.fitnessapp.others.Constants.NOTIFICATION_CHANNEL_ID
import com.example.fitnessapp.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.fitnessapp.others.Constants.NOTIFICATION_ID
import com.example.fitnessapp.others.Constants.PAUSE_SERVICE
import com.example.fitnessapp.others.Constants.START_OR_RESUME_SERVICE
import com.example.fitnessapp.others.Constants.STOP_SERVICE
import com.example.fitnessapp.others.TrackingUtility
import com.example.fitnessapp.ui.Fragments.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import timber.log.Timber.d

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>


 lateinit var fusedLocationProviderClient: FusedLocationProviderClient
 class TrackingService : LifecycleService(){

    private var isFirstRun = true

    companion object{
        val isTracking = MutableLiveData<Boolean>()
    // inner lines are polylines and there several  of polylines
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

          if(intent != null){

              when(intent.action){

                  START_OR_RESUME_SERVICE ->{
                      if(isFirstRun){
                         startForegroundService()
                         isFirstRun = false
                      }
                      else{
                          d("--START|RESUMESERVICE")
                      }

                  }
                  PAUSE_SERVICE ->{
                      d("--PAUSESERVICE")
                  }
                  STOP_SERVICE ->{
                      d("--STOP")
                  }
              }

          }

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun addEmptyPolyline () = pathPoints.value?.apply{
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also{
            it.action= Constants.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun addPathPoints(location: Location?){
        location?.let{
         val pos = LatLng(location.latitude,location.longitude)
            pathPoints.value?.apply{
                last().add(pos)
                pathPoints.postValue(this)
            }


        }

    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking:Boolean){
        if(isTracking){
          if(TrackingUtility.hasLocationPermission(this)){

              val request = LocationRequest().apply{
                interval = LOCTION_UPDATE_INTERVAL
                fastestInterval = LOCATION_FASTEST_UPDATE
                priority = PRIORITY_HIGH_ACCURACY
              }
              fusedLocationProviderClient.requestLocationUpdates(
                  request,
                  callBack,
                  Looper.getMainLooper()
              )
          }
        }
        else{
            fusedLocationProviderClient.removeLocationUpdates(callBack)
        }
    }

    val callBack = object :LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let{ locations ->

                    for(location in locations){
                        addPathPoints(location)
                        Timber.d("--newLocation:${location.latitude},${location.longitude}")
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startForegroundService(){

        addEmptyPolyline()
        isTracking.postValue(true)

       val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
           as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createNotficationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this,
            NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00.00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotficationChannel(notificationManager:NotificationManager){

      val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID
          , NOTIFICATION_CHANNEL_NAME,
          NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}