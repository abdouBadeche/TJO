package com.example.tjo.Entities

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query


/**
 * Created by User on 10/31/2017.
 */
interface ElasticSearchAPI {
    @GET("_search/")
    fun search(
        @HeaderMap headers: Map<String?, String?>?,
        @Query("default_operator") operator: String?,  //1st query (prepends '?')
        @Query("q") query: String? , //second query (prepends '&')
        @Query("sort") sort: String? = "Date_publication_FR:desc"
    ): Call<HitsObject?>?
}
