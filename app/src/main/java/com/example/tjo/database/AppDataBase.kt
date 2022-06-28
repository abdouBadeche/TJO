package com.example.tjo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tjo.Dao.*
import com.example.tjo.Entities.*
import com.example.tjo.Entitties.TexteSearched

@Database(entities = [TexteFav::class , TexteSearched::class  ] , version = 1)

abstract class AppDataBase : RoomDatabase() {
    abstract fun searchedDao(): TexteSearchedDao
    abstract fun favDao(): TexteFavDao

}
