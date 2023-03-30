
package com.example.fitnessapp.ui.Fragments
import android.app.Activity
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentTrackingBinding
import com.example.fitnessapp.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.fitnessapp.others.Constants.MAP_ZOOM
import com.example.fitnessapp.others.Constants.PAUSE_SERVICE
import com.example.fitnessapp.others.Constants.POLYLINE_COLOR
import com.example.fitnessapp.others.Constants.POLY_LINE_WIDTH
import com.example.fitnessapp.others.Constants.START_OR_RESUME_SERVICE
import com.example.fitnessapp.others.Constants.STOP_SERVICE
import com.example.fitnessapp.others.TrackingUtility
import com.example.fitnessapp.services.Polylines
import com.example.fitnessapp.services.TrackingService
import com.example.fitnessapp.ui.Fragments.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import dagger.hilt.android.AndroidEntryPoint

private lateinit var  mainViewModel: MainViewModel

private lateinit var fragmentTrackingBinding: FragmentTrackingBinding

private var myPolyline:Polylines = mutableListOf()

private var map:GoogleMap? = null

private var cancelMenu:Menu? = null


//   var isTracking = false

private var pathPoints = mutableListOf<Polyline>()

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

 private var curTimeInMillis = 0L
 private var isTracking:Boolean = false



 override fun onCreateView(
  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {

  fragmentTrackingBinding = FragmentTrackingBinding.inflate(inflater,container,false)
  setHasOptionsMenu(true)
  return fragmentTrackingBinding.root
 }



 override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
  super.onCreateOptionsMenu(menu, inflater)
 inflater.inflate(R.menu.tracking_menu,menu)
  cancelMenu =menu;

 }

 private fun showCancelDialog(){

  val cancelDialog = MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme)
   .setTitle("Cancel the Run?")
   .setMessage("Are you sure to cancel the current and delete its data?")
   .setIcon(R.drawable.ic_delete)
   .setPositiveButton("Yes"){_,_->

    stopRun();
   }
   .setNegativeButton("No"){dialogInteface,_,->

    dialogInteface.cancel()

   }.create()
  cancelDialog.show()
 }

 override fun onOptionsItemSelected(item: MenuItem): Boolean {
  when(item.itemId){
   R.id.cancel_run ->{
    showCancelDialog()
   }
  }
  return super.onOptionsItemSelected(item)
 }

 private fun stopRun() {

 sendCommandToService(STOP_SERVICE)
 findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
 }

 override fun onPrepareOptionsMenu(menu: Menu) {
  super.onPrepareOptionsMenu(menu)
  if(curTimeInMillis > 0L){
   cancelMenu?.get(0)?.isVisible = true
  }
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

  fragmentTrackingBinding.mapView.onCreate(savedInstanceState)

  fragmentTrackingBinding.btnToggleRun.setOnClickListener {

   toggleRun()
   Log.d("--sendCommandToService: ","done")
  }


  fragmentTrackingBinding.mapView.getMapAsync{

   map = it
   addAllPolylines()
  }
  subscribeToObservers()
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

 // when user change its position camera can also move with it.
 private fun moveCameraUser(){

  val googlePolylines: MutableList<com.google.android.gms.maps.model.Polyline> =
   myPolyline.map { polyline ->

    if(myPolyline.isNotEmpty() && myPolyline.last().isNotEmpty()){

     map?.animateCamera(
      CameraUpdateFactory.newLatLngZoom(
       myPolyline.last().last(),
       MAP_ZOOM
      ))
    }

   } as MutableList<Polyline>


 }

 private fun subscribeToObservers(){

  TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
   updateTracking(it)
  })
  TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {

   myPolyline = it
   // your code here
   addLatestPolylines()
   moveCameraUser()

  })

  TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
   curTimeInMillis = it
   val formattedTime = TrackingUtility.getFormatedStopWatchTime(curTimeInMillis, true)
   fragmentTrackingBinding.tvTimer.text = formattedTime
  })

 }
 private fun toggleRun(){
  if(isTracking){
   cancelMenu?.get(0)?.isVisible = true
   sendCommandToService(PAUSE_SERVICE)
  }
  else{
   sendCommandToService(START_OR_RESUME_SERVICE)
  }
 }

 // to get live date from our service
 private fun updateTracking(isTracking:Boolean){
  this.isTracking = isTracking
  if(!isTracking){
   fragmentTrackingBinding.btnToggleRun.text = "Start"
   fragmentTrackingBinding.btnFinishRun.visibility = View.VISIBLE
  }
  else{
   cancelMenu?.get(0)?.isVisible = true
   fragmentTrackingBinding.btnToggleRun.text = "Stop"
   fragmentTrackingBinding.btnFinishRun.visibility = View.GONE
  }
 }


 // if user rotate device activity will recreate and we loss polyline data
 // so to get all previous polylines after rotating we get from this function.

 private fun  addAllPolylines(){

  val googlePolylines: MutableList<com.google.android.gms.maps.model.Polyline> =
   myPolyline.map { polyline ->
    val options = PolylineOptions()
     .color(POLYLINE_COLOR)
     .width(POLY_LINE_WIDTH)
    options.addAll(polyline)
    map?.addPolyline(options)
   } as MutableList<Polyline>



//   for(polyline  in  pathPoints) {
//    val polylineOptions: PolylineOptions = PolylineOptions()
//     .color(POLYLINE_COLOR)
//     .width(POLY_LINE_WIDTH)
//     .addAll(polyline)
//    map?.addPolyline(polylineOptions)
//   }

 }

 // to get latest path of polyline
 private fun addLatestPolylines(){



  if(myPolyline.isNotEmpty() && myPolyline.last().size > 1){

   val preLastLong = myPolyline.last()[myPolyline.last().size-2] // it represent 2nd last location of map
   val lastLatLong  = myPolyline.last().last() // it represent last location of map
   // once we get both 2nd last and last location we can draw latest polyline between them.

   val polyOption = PolylineOptions()
    .color(POLYLINE_COLOR)
    .width(POLY_LINE_WIDTH)
    .add(preLastLong)
    .add(lastLatLong)

   map?.addPolyline(polyOption)
  }
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