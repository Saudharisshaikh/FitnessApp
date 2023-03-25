package com.example.fitnessapp.ui.Fragments

import android.Manifest
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.others.Constants.REQUEST_CODE_FOR_PERMISSON
import com.example.fitnessapp.others.TrackingUtility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


//@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run),EasyPermissions.PermissionCallbacks {

  //  private val mainViewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

        val floatActionButton :FloatingActionButton = view.findViewById(R.id.fab)
        floatActionButton.setOnClickListener {
            floatActionButton.findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

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