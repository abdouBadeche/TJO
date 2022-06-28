package com.example.tjo.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.tjo.R
import kotlinx.android.synthetic.main.filter_fragement.*
import kotlinx.android.synthetic.main.filter_fragement.view.*
import kotlinx.android.synthetic.main.texte_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class FilterFragment : DialogFragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View  = inflater.inflate(R.layout.filter_fragement , container  , false)

        rootView.btn_date_debut.setOnClickListener{ view ->
            clickDatePicker(view , 0)
        }


        rootView.btn_date_fin.setOnClickListener{ view ->
            clickDatePicker(view , 1)
        }




        var mPreferences: SharedPreferences = rootView.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        var lang = mPreferences.getString("LANG", "AR")



        if(lang.equals("FR")) {
            rootView.textView3.text = "Filtrer les résultats"
            rootView.filter_title_date_debut.text = "Date debut"
            rootView.filter_title_date_fin.text = "Date fin"
            rootView.filter_title_type.text = "Type"
            rootView.filter_title_secteur.text = "Secteur"
            rootView.filter_title_mot.text = "Mot clé"
            rootView.filter_title_organe.text = "Organe emetteur"
            rootView.submit_filter.text = "Filter"
        }else {
            rootView.textView3.text = "تصفية النتائج"
            rootView.filter_title_date_debut.text = "تاريخ البداية"
            rootView.filter_title_date_fin.text = "تاريخ النهاية"
            rootView.filter_title_type.text = "النوع"
            rootView.filter_title_secteur.text = "القطاع"
            rootView.filter_title_mot.text = "كلمة مفتاحية"
            rootView.filter_title_organe.text = "الجهة المصدرة"
            rootView.submit_filter.text = "إبحث"


        }


        rootView.submit_filter.setOnClickListener {
            val i: Intent = Intent()
                .putExtra("debut", rootView.tv_date_debut.text.toString())
                .putExtra("fin", rootView.tv_date_fin.text.toString())
                .putExtra("type", rootView.in_type.text.toString())
                .putExtra("secteur", rootView.in_secteur.text.toString())
                .putExtra("mot", rootView.in_mot.text.toString())
                .putExtra("organe", rootView.in_organe.text.toString())
            targetFragment !!.onActivityResult(101, Activity.RESULT_OK, i)
            dismiss()
        }

        return rootView
    }



    fun clickDatePicker(view : View , type : Int) {
        var today : Calendar = Calendar.getInstance() ;
        var year = today.get(Calendar.YEAR)
        var month = today.get(Calendar.MONTH)
        var day = today.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(  view.getContext()  ,
            DatePickerDialog.OnDateSetListener {
                    view, year, month, dayOfMonth ->
                                                if(type == 0) {
                                                    DateChange(year , month , dayOfMonth)
                                                }else {
                                                    DateChange2(year , month , dayOfMonth)
                                                }


            } , year , month ,day
        ).show()
    }

    fun DateChange(year:Int , month:Int  , dayOfMonth:Int ) {
        var debut : Calendar = Calendar.getInstance() ;
        debut.set(year , month ,dayOfMonth)

        var formatter = SimpleDateFormat("dd-MM-yyyy")

        tv_date_debut.text = formatter.format(debut.time)


    }


    fun DateChange2(year:Int , month:Int  , dayOfMonth:Int ) {
        var fin : Calendar = Calendar.getInstance() ;
        fin.set(year , month ,dayOfMonth)

        var formatter = SimpleDateFormat("dd-MM-yyyy")

        tv_date_fin.text = formatter.format(fin.time)


    }
}