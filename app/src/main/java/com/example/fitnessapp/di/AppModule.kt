package com.example.fitnessapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.fitnessapp.db.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.example.fitnessapp.others.Constants
import com.example.fitnessapp.others.Constants.KEY_NAME
import com.example.fitnessapp.others.Constants.KEY_TOGGLE_FIRST
import com.example.fitnessapp.others.Constants.RUNNING_DATABASE_NAME
import com.example.fitnessapp.others.Constants.SHARED_PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context

    )= Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideRunningDAO ( db:RunningDatabase)=db.getDAO()


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context)=
        app.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)


    @Singleton
    @Provides
    fun provideName(sharedPreferences: SharedPreferences) = sharedPreferences.getString(KEY_NAME,"")?:""


    @Singleton
    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences) = sharedPreferences.getFloat(KEY_NAME,80f)

    @Singleton
    @Provides
    fun provideFirstToggle(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_TOGGLE_FIRST,true)
}