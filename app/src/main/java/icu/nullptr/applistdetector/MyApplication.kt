package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
    }

    init {
        System.loadLibrary("applist_detector")
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}
