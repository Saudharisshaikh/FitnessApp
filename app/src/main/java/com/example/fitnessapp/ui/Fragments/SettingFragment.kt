package com.example.fitnessapp.ui.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentSettingsBinding
import com.example.fitnessapp.others.Constants.KEY_NAME
import com.example.fitnessapp.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_settings) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var fragmentSettingsBinding: FragmentSettingsBinding

    private fun applyChangesToSharedPref():Boolean{

       val name = fragmentSettingsBinding.etName.text.toString()
       val weight = fragmentSettingsBinding.etWeight.text.toString()

       if(name.isEmpty() || weight.isEmpty()){
           return false
       }
       sharedPreferences.edit()
           .putString(KEY_NAME,name)
           .putFloat(KEY_WEIGHT,weight.toFloat())
           .apply()
         return true

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldsFromSharedPref()

        fragmentSettingsBinding.btnApplyChanges.setOnClickListener {

            val success = applyChangesToSharedPref()
            if(success){

                Snackbar.make(view,"saved changes",Snackbar.LENGTH_LONG).show()
            }
            else{

                Snackbar.make(view,"Please fill all the fields...",Snackbar.LENGTH_LONG).show()
            }

        }

    }


    private fun loadFieldsFromSharedPref(){

        val name = sharedPreferences.getString(KEY_NAME,"")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT,80f)

        fragmentSettingsBinding.etName.setText(name)
        fragmentSettingsBinding.etWeight.setText(weight.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater,container,false)

        return fragmentSettingsBinding.root
    }
}