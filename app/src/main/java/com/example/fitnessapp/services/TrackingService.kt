package com.example.fitnessapp.services

import android.content.Intent
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LifecycleService
import com.example.fitnessapp.others.Constants.PAUSE_SERVICE
import com.example.fitnessapp.others.Constants.START_OR_RESUME_SERVICE
import com.example.fitnessapp.others.Constants.STOP_SERVICE
import timber.log.Timber.d
import timber.log.Timber.log

class TrackingService : LifecycleService(){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

          if(intent != null){

              when(intent.action){

                  START_OR_RESUME_SERVICE ->{
                      d("--START|RESUMESERVICE")
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
}