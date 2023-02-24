package com.example.fitnessapp.ui.Fragments.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnesstrackingapp.Repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel @Inject
constructor(val mainRepository: MainRepository
): ViewModel(){

}