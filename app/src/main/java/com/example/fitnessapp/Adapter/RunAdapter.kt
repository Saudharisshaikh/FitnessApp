package com.example.fitnessapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnessapp.R
import com.example.fitnessapp.db.Run
import com.example.fitnessapp.others.TrackingUtility
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter :RecyclerView.Adapter<RunAdapter.RunViewHolder>() {


    val differentCallback = object :DiffUtil.ItemCallback<Run>(){

        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
          return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
         return oldItem.hashCode() == newItem.hashCode()
        }
    }


    val differ = AsyncListDiffer(this,differentCallback)

    fun submitList(list:List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_run, parent, false)
        return RunViewHolder(view)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {

        val run = differ.currentList[position]
        holder.itemView.apply {
          Glide.with(this).load(run.img).into(holder.ivImage)

          val calender = Calendar.getInstance().apply {
              timeInMillis = run.timestamp
          }
          val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
          holder.tDate.text = dateFormat.format(calender.time)

          val avgSpeed = "${run.avgSpeedInKmh}Km/h"
          holder.avSpeed.text = avgSpeed

          val avgDistance = "${run.avgSpeedInKmh/1000f}km"
          holder.tDistance.text = avgDistance

          val avgTime = TrackingUtility.getFormatedStopWatchTime(run.timeInMills)
          holder.tTime.text = avgTime

          val avgCalories = "${run.caleriesBurned}kcal"
          holder.avCalories.text = avgCalories

        }

    }

    inner class RunViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){

     val ivImage : ImageView = itemview.findViewById(R.id.ivRunImage)
     val tDate :MaterialTextView = itemview.findViewById(R.id.tvDate)
     val tTime :MaterialTextView = itemview.findViewById(R.id.tvTime)
     val tDistance : MaterialTextView = itemview.findViewById(R.id.tvDistance)
     val avSpeed:MaterialTextView = itemview.findViewById(R.id.tvAvgSpeed)
     val avCalories :MaterialTextView = itemview.findViewById(R.id.tvCalories)



    }

}


