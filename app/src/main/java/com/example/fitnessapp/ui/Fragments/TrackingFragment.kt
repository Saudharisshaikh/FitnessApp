
package com.example.fitnessapp.ui.Fragments
import android.app.Activity
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentTrackingBinding
import com.example.fitnessapp.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.fitnessapp.others.Constants.START_OR_RESUME_SERVICE
import com.example.fitnessapp.services.TrackingService
import com.example.fitnessapp.ui.Fragments.viewmodels.MainViewModel
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint

  private lateinit var  mainViewModel: MainViewModel

  private lateinit var fragmentTrackingBinding: FragmentTrackingBinding

  private var map:GoogleMap? = null

 @AndroidEntryPoint
 class TrackingFragment : Fragment(R.layout.fragment_tracking) {


  override fun onCreateView(
   inflater: LayoutInflater,
   container: ViewGroup?,
   savedInstanceState: Bundle?
  ): View? {

    fragmentTrackingBinding = FragmentTrackingBinding.inflate(inflater,container,false)

  return fragmentTrackingBinding.root
  }



  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   super.onViewCreated(view, savedInstanceState)

   fragmentTrackingBinding.mapView.onCreate(savedInstanceState)

    fragmentTrackingBinding.btnToggleRun.setOnClickListener {

     sendCommandToService(START_OR_RESUME_SERVICE)
     Log.d("--sendCommandToService: ","done")
    }


   fragmentTrackingBinding.mapView.getMapAsync{

    map = it
   }
  }

  override fun onResume() {
   super.onResume()
   fragmentTrackingBinding.mapView.onResume()
  }

  override fun onStart() {
   super.onStart()

   fragmentTrackingBinding.mapView.onStart()
  }



  override fun onStop() {
   super.onStop()
   fragmentTrackingBinding.mapView.onStop()
  }

  override fun onPause() {
   super.onPause()
   fragmentTrackingBinding.mapView.onPause()
  }

  override fun onLowMemory() {
   super.onLowMemory()
   fragmentTrackingBinding.mapView.onLowMemory()
  }

  override fun onSaveInstanceState(outState: Bundle) {
   super.onSaveInstanceState(outState)
   fragmentTrackingBinding.mapView.onSaveInstanceState(outState)
  }

  private fun sendCommandToService(action:String) {


   val intent: Intent = Intent(requireContext(), TrackingService::class.java)
   intent.action = action
   requireContext().startService(intent)
  }


}