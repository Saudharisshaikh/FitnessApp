package com.example.fitnessapp.ui.Fragments

import android.Manifest
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.Adapter.RunAdapter
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentRunBinding
import com.example.fitnessapp.databinding.FragmentTrackingBinding
import com.example.fitnessapp.others.Constants.REQUEST_CODE_FOR_PERMISSON
import com.example.fitnessapp.others.SortType
import com.example.fitnessapp.others.TrackingUtility
import com.example.fitnessapp.ui.Fragments.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run),EasyPermissions.PermissionCallbacks {


    private val  mainViewModel: MainViewModel by viewModels()
    private lateinit var fragmentRunBinding: FragmentRunBinding
   lateinit var runAdapter :RunAdapter

    //  private val mainViewModel: MainViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentRunBinding = FragmentRunBinding.inflate(inflater, container, false)

        return fragmentRunBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setRv()

        when(mainViewModel.sortType){

            SortType.DATE -> fragmentRunBinding.spFilter.setSelection(0)
            SortType.RUNNING_TIME -> fragmentRunBinding.spFilter.setSelection(1)
            SortType.DISTANCE -> fragmentRunBinding.spFilter.setSelection(2)
            SortType.AVG_SPEED -> fragmentRunBinding.spFilter.setSelection(3)
            SortType.CALORIES_BURNED -> fragmentRunBinding.spFilter.setSelection(4)


        }
        fragmentRunBinding.spFilter.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when(position){
                    0 -> mainViewModel.sortRuns(SortType.DATE)
                    1 -> mainViewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> mainViewModel.sortRuns(SortType.DISTANCE)
                    3 -> mainViewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> mainViewModel.sortRuns(SortType.CALORIES_BURNED)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        mainViewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })


        fragmentRunBinding.fab.setOnClickListener {
            fragmentRunBinding.fab.findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
//        val floatActionButton :FloatingActionButton = view.findViewById(R.id.fab)
//        floatActionButton.setOnClickListener {
//            floatActionButton.findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
//        }

    }


    private fun setRv() = fragmentRunBinding.rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissions(){

        if(TrackingUtility.hasLocationPermission(requireContext())){
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this,
                "You need to accept this permission to use this app.",
                REQUEST_CODE_FOR_PERMISSON,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )


        }
        else{

            EasyPermissions.requestPermissions(this,
                "You need to accept this permission to use this app.",
                REQUEST_CODE_FOR_PERMISSON,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
        else{
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}