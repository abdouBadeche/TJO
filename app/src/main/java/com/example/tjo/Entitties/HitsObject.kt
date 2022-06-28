package com.example.tjo.Entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by User on 10/31/2017.
 */
class HitsObject {
    @SerializedName("hits")
    @Expose
    var hits: HitsList? = null
}
