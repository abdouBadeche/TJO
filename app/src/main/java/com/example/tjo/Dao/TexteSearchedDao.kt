package com.example.tjo.Dao

import androidx.room.*
import com.example.tjo.Entitties.Texte
import com.example.tjo.Entitties.TexteSearched

@Dao
interface TexteSearchedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTexteSearched(texteSearched: TexteSearched)

    @Update
    fun updateTexteSearched(texteSearched: TexteSearched)

    @Query("DELETE FROM texte_searched_table WHERE NumSGG == :idTexteSearched")
    fun deleteTexteSearched(idTexteSearched: String)

    @Query("SELECT * FROM texte_searched_table WHERE NumSGG == :idTexteSearched")
    fun getTexteSearchedById(idTexteSearched: String): List<TexteSearched>

    @Query("SELECT texte_searched_table.* FROM  texte_searched_table")
    fun getTexteSearchedList(): List<TexteSearched>


}