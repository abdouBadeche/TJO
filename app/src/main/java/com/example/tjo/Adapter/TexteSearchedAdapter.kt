package com.example.tjo.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tjo.Entitties.Texte
import com.example.tjo.Entitties.TexteSearched
import com.example.tjo.R
import com.example.tjo.database.ServiceDB
import com.example.tjo.fragments.TextePageFragment
import com.example.tjo.fragments.TexteSearchedFragment
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TexteSearchedAdapter(var msg: List<TexteSearched>):RecyclerView.Adapter<TexteSearchedAdapter.ViewHolder>()  {



    val simpleDate = SimpleDateFormat("dd/M/yyyy")

    var racinesFilterList = ArrayList<TexteSearched>()


    private  var dataSet= arrayListOf<TexteSearched>()



    init {
        dataSet.addAll(msg.asReversed())
        racinesFilterList = dataSet
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var date_pub: TextView = itemView.findViewById(R.id.tvSearchedDatePub)
        var date_sign: TextView = itemView.findViewById(R.id.tvSearchedDateSign)
        var text: TextView = itemView.findViewById(R.id.tvSearchedText)
        var type: TextView = itemView.findViewById(R.id.tvSearchedType)
        var annee: TextView = itemView.findViewById(R.id.tvSearchedTypeYear)
        var organe: TextView = itemView.findViewById(R.id.tvSearchedOrgane)
        var deleteItem: ImageView = itemView.findViewById(R.id.delete_item_searched)
        var mPreferences: SharedPreferences = itemView.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        var lang = mPreferences.getString("LANG", "AR")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewObj = LayoutInflater.from(parent.context).inflate(R.layout.texte_searched_item, parent, false)
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


        holder.deleteItem.setOnClickListener {
            ServiceDB.database.searchedDao().deleteTexteSearched(racinesFilterList[position].NumSGG)
            val histRacineFragment =TexteSearchedFragment()

            val activity = it!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment,  histRacineFragment)

                commit()
            }
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {


                val activity = v!!.context as AppCompatActivity


                var texte = Texte() ;

                texte.NumSGG = racinesFilterList[position].NumSGG
                texte.NumJO = racinesFilterList[position].NumJO ;
                texte.Type_Texte_FR = racinesFilterList[position].Type_Texte_FR ;
                texte.Type_Texte_AR = racinesFilterList[position].Type_Texte_AR ;
                texte.Page_FR = racinesFilterList[position].Page_FR ;
                texte.Page_AR = racinesFilterList[position].Page_AR ;
                texte.Date_publication_FR = racinesFilterList[position].Date_publication_FR ;
                texte.Date_Publication_AR = racinesFilterList[position].Date_Publication_AR ;
                texte.Date_Signature_FR = racinesFilterList[position].Date_Signature_FR ;
                texte.Date_Signature_AR = racinesFilterList[position].Date_Signature_AR ;
                texte.Sommaire_FR = racinesFilterList[position].Sommaire_FR ;
                texte.Sommaire_AR = racinesFilterList[position].Sommaire_AR ;
                texte.ATexte = racinesFilterList[position].ATexte ;
                texte.FTexte = racinesFilterList[position].FTexte ;
                texte.Organe_FR = racinesFilterList[position].Organe_FR ;
                texte.Organe_AR = racinesFilterList[position].Organe_AR ;
                texte.AnneeJO = racinesFilterList[position].AnneeJO ;
                texte.F_PDFFileName = racinesFilterList[position].F_PDFFileName ;
                texte.A_PDFFileName = racinesFilterList[position].A_PDFFileName ;
                val textePageFragment = TextePageFragment( texte)

               activity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment,  textePageFragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }
        )
    }




}