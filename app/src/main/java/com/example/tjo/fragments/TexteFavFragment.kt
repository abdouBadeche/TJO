package com.example.tjo.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tjo.Adapter.TexteFavAdapter
import com.example.tjo.Entitties.Texte

import com.example.tjo.R
import com.example.tjo.database.ServiceDB

class TexteFavFragment:Fragment(R.layout.recycler_fav_texte_fragment){

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TexteFavAdapter.ViewHolder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_fav_texte)
        val msg = ServiceDB.database.favDao()?.getTexteFavList()

        recyclerView?.layoutManager = this.layoutManager


        var mPreferences: SharedPreferences = view.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        var lang = mPreferences.getString("LANG", "AR")

        val title_fragment = view?.findViewById<TextView>(R.id.tv_fav_list_title)

        if(lang.equals("FR")) {
            title_fragment.text = "Les Favs Textes"
        }else {
            title_fragment.text = "النصوص المفضلة"

        }


        adapter = TexteFavAdapter(msg)


        recyclerView?.adapter = this.adapter

    }







}