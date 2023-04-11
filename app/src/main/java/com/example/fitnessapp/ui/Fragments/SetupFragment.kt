package com.example.fitnessapp.ui.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentSetupBinding
import com.example.fitnessapp.others.Constants.KEY_NAME
import com.example.fitnessapp.others.Constants.KEY_TOGGLE_FIRST
import com.example.fitnessapp.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private lateinit var fragmentSetupBinding :FragmentSetupBinding

    @set:Inject
    var isFirstTimeApp = true

    @Inject
     lateinit var sharedPref:SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!isFirstTimeApp){

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment,true)
                .build()

            findNavController().navigate(
                R.id.runFragment,
                savedInstanceState,
                navOptions
            )
        }

        fragmentSetupBinding.tvContinue.setOnClickListener {

            val success = writePersonalDataToSP()
            if(success){
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            }
            else{

                Snackbar.make(requireView(),"Please fill all fields ...",Snackbar.LENGTH_LONG).show()
            }


        }


//        val buttonContinue:TextView= view.findViewById(R.id.tvContinue)
//        buttonContinue.setOnClickListener {
//
//            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         fragmentSetupBinding = FragmentSetupBinding.inflate(inflater,container,false)

        return fragmentSetupBinding.root
    }

    private fun writePersonalDataToSP() :Boolean{

     val name = fragmentSetupBinding.etName.text.toString()
     val weight = fragmentSetupBinding.etWeight.text.toString()
     if(name.isEmpty() || weight.isEmpty()){
         return false
     }
     sharedPref.edit()
         .putString(KEY_NAME,name)
         .putFloat(KEY_WEIGHT,weight.toFloat())
         .putBoolean(KEY_TOGGLE_FIRST,false)
         .apply()

        val toolbarText = "Let's go ${name}"
        return true

    }

}