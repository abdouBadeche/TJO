package com.example.tjo.room

import android.app.Application
import com.example.tjo.database.ServiceDB

class appR: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceDB.context=applicationContext
    }
}