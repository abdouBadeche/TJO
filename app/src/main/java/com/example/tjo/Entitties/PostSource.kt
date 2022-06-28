package com.example.tjo.Entities

import com.example.tjo.Entitties.Texte
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by User on 10/31/2017.
 */
class PostSource {
    @SerializedName("_source")
    @Expose
    var post: Texte? = null
}
