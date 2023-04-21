package com.example.fitnessapp.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentStatisticsBinding
import com.example.fitnessapp.others.TrackingUtility
import com.example.fitnessapp.ui.Fragments.viewmodels.StatisticsViewModel
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