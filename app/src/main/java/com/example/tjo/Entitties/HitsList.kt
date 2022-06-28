package com.example.tjo.Entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by User on 10/31/2017.
 */
class HitsList {
    @SerializedName("hits")
    @Expose
    var postIndex: List<PostSource>? = null
}
