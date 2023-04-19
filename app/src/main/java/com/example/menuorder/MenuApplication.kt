package com.example.menuorder

import android.app.Application
import com.example.menuorder.data.AppContainer
import com.example.menuorder.data.AppDataContainer

class MenuApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}