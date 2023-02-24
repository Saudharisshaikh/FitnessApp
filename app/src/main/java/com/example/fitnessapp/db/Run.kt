package com.example.fitnessapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

 @Entity(tableName = "running_table")
data class Run(
    var img:Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKmh: Float = 0f,
    var distanceInMeters : Int = 0,
    var timeInMills : Long = 0L,
    var caleriesBurned:Int = 0
) {
   @PrimaryKey(autoGenerate = true)
   var id:Int? = null
}