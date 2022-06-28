package com.example.tjo.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room


@SuppressLint("StaticFieldLeak")
object ServiceDB{
    lateinit var context: Context
    val database by lazy {
        Room.databaseBuilder(context, AppDataBase::class.java,"ap_tjo")
            .allowMainThreadQueries()
            .build()
    }
}