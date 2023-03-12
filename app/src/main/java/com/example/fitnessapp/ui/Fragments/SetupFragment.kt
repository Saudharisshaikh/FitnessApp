package com.example.fitnessapp.ui.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R


class SetupFragment : Fragment(R.layout.fragment_setup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonContinue:TextView= view.findViewById(R.id.tvContinue)
        buttonContinue.setOnClickListener {

            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }
    }
}