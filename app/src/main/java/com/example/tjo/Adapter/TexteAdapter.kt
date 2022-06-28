package com.example.tjo.Adapter

/*
import com.example.quran.database.ServiceDB
import com.example.quran.fragments.VersetFragment

 */

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.tjo.Entities.ElasticSearchAPI
import com.example.tjo.Entities.HitsList
import com.example.tjo.Entities.HitsObject
import com.example.tjo.Entitties.Texte
import com.example.tjo.Entitties.TexteSearched
import com.example.tjo.R
import com.example.tjo.database.ServiceDB
import com.example.tjo.fragments.TextePageFragment
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TexteAdapter(var msg: List<Texte> , var user_search : Boolean = false ):RecyclerView.Adapter<TexteAdapter.ViewHolder>() , Filterable {

    val simpleDate = SimpleDateFormat("dd/M/yyyy")

    var racinesFilterList = ArrayList<Texte>()

    private  var dataSet= arrayListOf<Texte>()

    init {
        dataSet.addAll(msg)
        racinesFilterList = dataSet
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var date_pub: TextView = itemView.findViewById(R.id.tvDatePub)
        var date_sign: TextView = itemView.findViewById(R.id.tvDateSign)
        var text: TextView = itemView.findViewById(R.id.tvText)
        var type: TextView = itemView.findViewById(R.id.tvType)
        var annee: TextView = itemView.findViewById(R.id.tvTypeYear)
        var organe: TextView = itemView.findViewById(R.id.tvOrgane)
        var mPreferences: SharedPreferences = itemView.context.applicationContext.getSharedPreferences("LANG", MODE_PRIVATE)
        var lang = mPreferences.getString("LANG", "AR")




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var viewObj = LayoutInflater.from(parent.context).inflate(R.layout.texte_item, parent, false)
        return ViewHolder(viewObj)
    }


    override fun getItemCount(): Int {
        return racinesFilterList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        val formatter = SimpleDateFormat("yyyyMMdd")
        val text = racinesFilterList[position].Date_publication_FR.toString()
        val date : Date = formatter.parse(text)

        val currentDate = simpleDate.format(date)
        holder.date_pub.text = currentDate.toString() ;


        val formatter2 = SimpleDateFormat("yyyyMMdd")
        val text2 = racinesFilterList[position].Date_Signature_FR.toString()
        val date2 : Date = formatter2.parse(text2)

        val currentDate2 = simpleDate.format(date2)
        holder.date_sign.text = currentDate2.toString() ;
        holder.annee.text = racinesFilterList[position].AnneeJO.toString() ;




        if(holder.lang.equals("FR")) {

            holder.text.text = racinesFilterList[position].FTexte.toString() ;
            holder.type.text = racinesFilterList[position].Type_Texte_FR.toString() ;
            holder.organe.text = racinesFilterList[position].Organe_FR.toString() ;

        }else {

            holder.text.text = racinesFilterList[position].ATexte.toString() ;
            holder.type.text = racinesFilterList[position].Type_Texte_AR.toString() ;
            holder.organe.text = racinesFilterList[position].Organe_AR.toString() ;

        }


       holder.itemView.setOnClickListener(object : View.OnClickListener{
           override fun onClick(v: View?) {
               val activity = v!!.context as AppCompatActivity

               if( user_search ){
                   var texte_searched = TexteSearched(
                       racinesFilterList[position].NumSGG ,
                       racinesFilterList[position].NumJO ,
                       racinesFilterList[position].Type_Texte_FR ,
                       racinesFilterList[position].Type_Texte_AR ,
                       racinesFilterList[position].Page_FR ,
                       racinesFilterList[position].Page_AR ,
                       racinesFilterList[position].Date_publication_FR ,
                       racinesFilterList[position].Date_Publication_AR ,
                       racinesFilterList[position].Date_Signature_FR ,
                       racinesFilterList[position].Date_Signature_AR ,
                       racinesFilterList[position].Sommaire_FR ,
                       racinesFilterList[position].Sommaire_AR ,
                       racinesFilterList[position].ATexte ,
                       racinesFilterList[position].FTexte ,
                       racinesFilterList[position].Organe_FR ,
                       racinesFilterList[position].Organe_AR ,
                       racinesFilterList[position].AnneeJO ,
                       racinesFilterList[position].F_PDFFileName ,
                       racinesFilterList[position].A_PDFFileName ,
                   )
                   ServiceDB.database.searchedDao().deleteTexteSearched(texte_searched.NumSGG )
                   ServiceDB.database.searchedDao().insertTexteSearched(texte_searched ) ;
               }




               val textFragmenet = TextePageFragment( racinesFilterList[position] ) ;

               activity.supportFragmentManager.beginTransaction().apply {
                   replace(R.id.flFragment,  textFragmenet)
                   addToBackStack(null)
                   commit()
               }
           }
       }
       )


    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    racinesFilterList = dataSet
                } else {

                    val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl("http://10.0.24.63:9200/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val searchAPI = retrofit.create(
                        ElasticSearchAPI::class.java
                    )

                    val headerMap = HashMap<String?, String?>()
                    headerMap["Authorization"] = Credentials.basic("elastic", "a+P5PUb-ZX6-a--p-vE1")

                    var searchString = "*"


                    if (!charSearch.equals("")) {
                        searchString = "$searchString FTexte:$charSearch*"
                    }

                    user_search = true

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




                                var textes : ArrayList<Texte> = arrayListOf() ;

                                for (i in hitsList!!.postIndex!!.indices) {
                                    Log.d("data", "onResponse: data: " + hitsList!!.postIndex!![i].post.toString())
                                    textes.add(hitsList!!.postIndex!![i].post as Texte) ;
                                }




                                racinesFilterList = textes

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
                val filterResults = FilterResults()
                filterResults.values = racinesFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                racinesFilterList = results?.values as ArrayList<Texte>
                notifyDataSetChanged()
            }

        }
    }



}