package com.nathaniel.carryapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.presentation.ui.service.ServiceLocator
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var apiRepository: ApiRepository

    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        Timber.plant(Timber.DebugTree())
        ServiceLocator.apiRepository = apiRepository
    }
}