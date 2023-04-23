package com.example.fitnessapp.ui.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentStatisticsBinding
import com.example.fitnessapp.others.CustomMarkerView
import com.example.fitnessapp.others.TrackingUtility
import com.example.fitnessapp.ui.Fragments.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

      private val statisticsViewModel: StatisticsViewModel by viewModels()

      lateinit var binding: FragmentStatisticsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setBarChart()

    }


    private fun setBarChart(){
        binding.barChart.xAxis.apply {

            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisLeft.apply {

            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)

        }
        binding.barChart.axisRight.apply {

            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)

        }

        binding.barChart.apply {
            description.text = "Avg Speed over time"
            legend.isEnabled = false
        }
    }





    private fun subscribeToObservers(){

        statisticsViewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {

            it?.let {

                val totalTimeRun = TrackingUtility.getFormatedStopWatchTime(it)
                 binding.tvTotalTime.text = totalTimeRun
            }
        })

        statisticsViewModel.totalDistance.observe(viewLifecycleOwner, Observer {

            it?.let {
                val km = it/1000f
                val totalDistance = round(km * 10f)/10f
                val totalDistanceString = "${totalDistance}km"
                binding.tvTotalDistance.text = totalDistanceString
            }
        })

        statisticsViewModel.avgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it * 10)/10
                val avgSpeedString = "${avgSpeed}km/h"
                binding.tvAverageSpeed.text = avgSpeedString
            }

        })

        statisticsViewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {

            it?.let {
                val cal = "${it}kcal"
                binding.tvTotalCalories.text = cal
            }
        })

       statisticsViewModel.getRunSortedByDate.observe(viewLifecycleOwner, Observer {

           it?.let{

            val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(),it[i].avgSpeedInKmh) }
            val barDataSet = BarDataSet(allAvgSpeeds,"Avg Speed Over Time").apply {
                valueTextColor = Color.WHITE
                color = ContextCompat.getColor(requireContext(),R.color.colorAccent)
            }
            binding.barChart.data = BarData(barDataSet)
            binding.barChart.marker = CustomMarkerView(it.reversed(),requireContext(),R.layout.marker_view)
            binding.barChart.invalidate()
           }
       })
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStatisticsBinding.inflate(inflater,container,false)
        return binding.root
    }

}