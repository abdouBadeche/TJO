package com.example.tjo.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tjo.Adapter.TexteAdapter
import com.example.tjo.Entities.ElasticSearchAPI
import com.example.tjo.Entities.HitsList
import com.example.tjo.Entities.HitsObject
import com.example.tjo.R
import com.example.tjo.Entitties.Texte
import kotlinx.android.synthetic.main.filter_fragement.*
import kotlinx.android.synthetic.main.recycler_texte_fragment.*
import okhttp3.Credentials
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TextesFragment(var list: List<Texte>) : Fragment(R.layout.recycler_texte_fragment) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TexteAdapter.ViewHolder>? = null

    var lang : String = "FR"

    var filtred : Boolean = false ;
    var query_filter : String? = "" ;


    var recyclerView  : RecyclerView? = null  ;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var mPreferences: SharedPreferences = view.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        lang = mPreferences.getString("LANG", "AR").toString()

        if(lang.equals("FR")) {
            search_input.queryHint = "Recherche..." ;
        }else {
            search_input.queryHint = "بحث..." ;
        }



        layoutManager = LinearLayoutManager(requireContext())


        recyclerView = view?.findViewById<RecyclerView>(R.id.racine_recycler) ;


        recyclerView?.layoutManager = this.layoutManager


        getDate() ;





        btn_filter.setOnClickListener {
            var dialog = FilterFragment() ;
            dialog.setTargetFragment(this, 101);

            val activity = it.context as AppCompatActivity

            dialog.show(activity.supportFragmentManager , "filterFragment" )
        }

        search_input.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (adapter as TexteAdapter).filter.filter(newText)
                query_filter  = newText ;
                getDate(query_filter.toString())
                return false
            }

        })

    }

    fun getDate(param : String = "" , type : String = "" , secteur : String = ""  , mot : String = ""  , organe : String = "" , date : String = "" ) {

        var user_searche = false ;

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.24.63:9200/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val searchAPI = retrofit.create(
            ElasticSearchAPI::class.java
        )

        val headerMap = HashMap<String?, String?>()
        headerMap["Authorization"] = Credentials.basic("elastic", "a+P5PUb-ZX6-a--p-vE1")


        var searchString = ""

        if (lang.equals("FR")) {
            if (!param.equals("")) { searchString = "$searchString FTexte:$param*" }
            if (!type.equals("")) { searchString = "$searchString Type_Texte_FR:$type*" }
            if (!organe.equals("")) { searchString = "$searchString Organe_FR:$organe*" }
        }else {
            if (!param.equals("")) { searchString = "$searchString ATexte:$param*" }
            if (!type.equals("")) { searchString = "$searchString Type_Texte_AR:$type*" }
            if (!organe.equals("")) { searchString = "$searchString Organe_AR:$organe*" }
        }


        if (!secteur.equals("")) { searchString = "$searchString Secteurs.\\*:$secteur*" }



        if (!mot.equals("")) { searchString = "$searchString FR_Keywords.\\*:$mot*" }

        if (!date.equals("")) { searchString = "$searchString Date_publication_FR:$date" }


/*


        if ( date_debut > 0) {
            searchString = "$searchString Date_publication_FR:$date_debut"
        }
        if (date_fin  > 0) {
            searchString = "$searchString Date_publication_AR:$date_fin"
        }

        if (!mPrefStateProv.equals("")) {
            searchString = "$searchString state_province:$mPrefStateProv"
        }
        if (!mPrefCountry.equals("")) {
            searchString = "$searchString country:$mPrefCountry"
        }

 */


        if (searchString.equals("")) {
            searchString = "*"
        }else {
            user_searche = true
        }


        adapter = TexteAdapter(list , user_searche)



        recyclerView?.adapter = adapter

        val call = searchAPI.search(headerMap , "AND" , searchString )

        call?.enqueue(object : Callback<HitsObject?> {

            override fun onResponse(call: Call<HitsObject?>, response: Response<HitsObject?>) {
                var hitsList: HitsList? = HitsList()
                var jsonResponse: String? = ""
                try {
                    Log.d("response", "onResponse: server response: $response")
                    if (response.isSuccessful) {
                        hitsList = response.body()!!.hits
                    } else {
                        jsonResponse = response.errorBody()!!.string()
                    }
                    Log.d("hits", "onResponse: hits: $hitsList")




                    var todos = arrayOfNulls<String>(hitsList!!.postIndex!!.size)
                    var textes = emptyList<Texte>() ;
                    textes = emptyList() ;
                    for (i in hitsList!!.postIndex!!.indices) {
                        Log.d("data", "onResponse: data: " + hitsList!!.postIndex!![i].post.toString())
                        textes += hitsList!!.postIndex!![i].post as Texte ;
                        todos[i] = ""+hitsList!!.postIndex!![i].post?.Type_Texte_FR
                    }


                    list = textes ;

                    adapter = TexteAdapter(list)



                    recyclerView?.adapter = adapter

                    //Log.d("size", "onResponse: size: " + todosList.size)
                    //setup the list of posts
                }  catch (e: IOException) {
                    Log.e("error", "onResponse: IOException: " + e.message)
                }
            }

            override fun onFailure(call: Call<HitsObject?>, t: Throwable) {
                Log.e("onFailure", "onFailure: " + t.message)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {

                val date_debut_filter = data!!.getStringExtra("debut") ;
                val date_fin_filter =data!!.getStringExtra("fin") ;
                Log.w("returned_filter debut : " , date_debut_filter.toString() )
                Log.w("returned_filter fin : " , date_fin_filter.toString() )

                var  type_filter = "" ;
                var  secteur_filter = "" ;
                var  mot_filter = "" ;
                var  organe_filter = "" ;


                var datee = "" ;

                if(data.hasExtra("type")) type_filter = data!!.getStringExtra("type").toString() ;
                if(data.hasExtra("secteur")) secteur_filter = data!!.getStringExtra("secteur").toString() ;
                if(data.hasExtra("mot")) mot_filter = data!!.getStringExtra("mot").toString() ;
                if(data.hasExtra("organe")) organe_filter = data!!.getStringExtra("organe").toString() ;

                if(data.hasExtra("debut") && !date_debut_filter.equals("")  && data.hasExtra("fin")  && !date_fin_filter.equals("") ) {
                    var formatter = SimpleDateFormat("yyyyMMdd")

                    var date_first : Date = SimpleDateFormat("dd-MM-yyyy").parse(date_debut_filter.toString()) ;
                    val date_first_format = formatter.format(date_first.time)

                    var date_end : Date = SimpleDateFormat("dd-MM-yyyy").parse(date_fin_filter.toString()) ;
                    val date_end_format = formatter.format(date_end.time)

                    datee = "["+date_first_format.toString()+" TO "+date_end_format.toString()+"]"

                    Log.w("returned_filter fin : " , datee )
                }


                data.hasExtra("type")
                getDate(
                    query_filter.toString() ,
                    type_filter ,
                    secteur_filter ,
                    mot_filter ,
                    organe_filter ,
                    datee
                ) ;



            }
        }
    }






}