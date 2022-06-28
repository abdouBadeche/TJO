package com.example.tjo.Dao

import androidx.room.*
import com.example.tjo.Entities.TexteFav
import com.example.tjo.Entitties.Texte

@Dao
interface TexteFavDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTexteFav(texteFav: TexteFav)

    @Update
    fun updateTexteFav(texteFav: TexteFav)

    @Query("DELETE FROM fav_texte WHERE NumSGG == :idTexteFav")
    fun deleteTexteFav(idTexteFav: String)

    @Query("SELECT * FROM fav_texte WHERE NumSGG == :idTexteFav")
    fun getTexteFavById(idTexteFav: String): List<TexteFav>

    @Query("SELECT fav_texte.* FROM  fav_texte")
    fun getTexteFavList(): List<TexteFav>


}