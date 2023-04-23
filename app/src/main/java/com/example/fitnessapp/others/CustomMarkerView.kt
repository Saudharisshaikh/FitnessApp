package com.example.fitnessapp.others

import android.content.Context
import android.widget.TextView
import com.example.fitnessapp.R
import com.example.fitnessapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs:List<Run>,
    c:Context,
    layoutId:Int
): MarkerView(c,layoutId)
{

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f,-height.toFloat())

    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        if(e== null){
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val textDate :TextView = findViewById(R.id.tvDate_marker)
        val textDuration :TextView = findViewById(R.id.tvDuration_marker)
        val textAvgSpeed : TextView = findViewById(R.id.tvAvgSpeed_marker)
        val textDistance :TextView = findViewById(R.id.tvDistance_marker)
        val textCaloriesBurned:TextView = findViewById(R.id.tvCaloriesBurned_marker)


        val calender = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            textDate.text = dateFormat.format(calender.time)

        val avgSpeed = "${run.avgSpeedInKmh}Km/h"
        textAvgSpeed.text = avgSpeed

        val avgDistance = "${run.avgSpeedInKmh/1000f}km"
        textDistance.text = avgDistance

        val avgTime = TrackingUtility.getFormatedStopWatchTime(run.timeInMills)
        textDuration.text = avgTime

        val avgCalories = "${run.caleriesBurned}kcal"
        textCaloriesBurned.text = avgCalories

        }

    }
