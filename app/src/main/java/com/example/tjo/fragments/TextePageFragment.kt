package com.example.tjo.fragments

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Paint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tjo.R

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import java.io.IOException

import android.provider.ContactsContract
import android.text.Layout
import android.text.style.AlignmentSpan
import android.util.Log
import android.widget.*
import com.example.tjo.Entities.TexteFav
import com.example.tjo.Entitties.Texte
import com.example.tjo.Entitties.TexteSearched
import com.example.tjo.database.ServiceDB
import kotlinx.android.synthetic.main.texte_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class TextePageFragment(var texte: Texte) : Fragment(R.layout.texte_fragment){

    val simpleDate = SimpleDateFormat("dd/M/yyyy")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var mPreferences: SharedPreferences = view.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        var lang = mPreferences.getString("LANG", "AR")



        if(lang.equals("FR")) {
            tvFrTextAr.text = "Description"
            tvFrTextAr.textAlignment =  View.TEXT_ALIGNMENT_TEXT_START ;
            tvTextAr2.text = "l’organe émetteur"
            tvTextAr2.textAlignment =  View.TEXT_ALIGNMENT_TEXT_START ;
            tvFragType.text = ""+texte.Type_Texte_FR ;
            tvFragDescription.text = ""+texte.Sommaire_FR ;

            tvFragText.text = ""+texte.FTexte ;
            tvFragOrgane.text = ""+texte.Organe_FR ;
            tv_pdf_version_fr.text = "Version FR"
            tv_pdf_version_ar.text = "Version AR"
        }else {
            tvFrTextAr.text = "الوصف"
            tvFrTextAr.textAlignment =  View.TEXT_ALIGNMENT_TEXT_END ;
            tvTextAr2.text = "جهة الإصدار"
            tvTextAr2.textAlignment =  View.TEXT_ALIGNMENT_TEXT_END ;
            tvFragType.text = ""+texte.Type_Texte_AR ;
            tvFragDescription.text = ""+texte.Sommaire_AR ;

            tvFragText.text = ""+texte.ATexte ;
            tvFragOrgane.text = ""+texte.Organe_AR ;

            tv_pdf_version_fr.text = "النسخة الفرنسية"
            tv_pdf_version_ar.text = "النسخة العربية"

        }

        tvFragTypeYear.text = ""+texte.AnneeJO ;

        val formatter = SimpleDateFormat("yyyyMMdd")
        val text = texte.Date_publication_FR.toString()
        val date : Date = formatter.parse(text)

        val currentDate = simpleDate.format(date)
        tvdatePubPage.text = currentDate.toString() ;


        val formatter2 = SimpleDateFormat("yyyyMMdd")
        val text2 = texte.Date_Signature_FR.toString()
        val date2 : Date = formatter2.parse(text2)

        val currentDate2 = simpleDate.format(date2)
        tvdateSignPage.text = currentDate2.toString() ;

        val fav_items = ServiceDB.database.favDao().getTexteFavById(texte.NumSGG)
        var faved = false

        if(fav_items?.size > 0 ) {
            faved = true
            btn_fav.setImageResource(R.drawable.ic_bookmark)
        }else {
            btn_fav.setImageResource(R.drawable.ic_bookmark_disabled)
        }

        btn_fav.setOnClickListener {
            if(faved) {
                btn_fav.setImageResource(R.drawable.ic_bookmark_disabled)
                ServiceDB.database.favDao().deleteTexteFav(texte.NumSGG)
                faved = false
            }else {
                btn_fav.setImageResource(R.drawable.ic_bookmark)

                var texte_fav = TexteFav(
                    texte.NumSGG ,
                    texte.NumJO ,
                    texte.Type_Texte_FR ,
                    texte.Type_Texte_AR ,
                    texte.Page_FR ,
                    texte.Page_AR ,
                    texte.Date_publication_FR ,
                    texte.Date_Publication_AR ,
                    texte.Date_Signature_FR ,
                    texte.Date_Signature_AR ,
                    texte.Sommaire_FR ,
                    texte.Sommaire_AR ,
                    texte.ATexte ,
                    texte.FTexte ,
                    texte.Organe_FR ,
                    texte.Organe_AR ,
                    texte.AnneeJO ,
                    texte.F_PDFFileName ,
                    texte.A_PDFFileName ,
                )
                ServiceDB.database.favDao().insertTexteFav(texte_fav)
                faved = true
            }
        }


        pdf_btn.setOnClickListener {

            val url = "https://www.joradp.dz/FTP/JO-FRANCAIS/"+texte.AnneeJO+"/"+texte.F_PDFFileName ;

            val pdfFragmenet = TextePdfFragment(url) ;

            activity?.supportFragmentManager!!.beginTransaction().apply {
                replace(R.id.flFragment,  pdfFragmenet)
                addToBackStack(null)
                commit()
            }
        }
        pdf_btn_ar.setOnClickListener {

            val url = "https://www.joradp.dz/FTP/jo-arabe/" +texte.AnneeJO+"/"+texte.A_PDFFileName ;

            val pdfFragmenet = TextePdfFragment(url) ;

            activity?.supportFragmentManager!!.beginTransaction().apply {
                replace(R.id.flFragment,  pdfFragmenet)
                addToBackStack(null)
                commit()
            }
        }

        shareBtn.setOnClickListener {
            var dialog = ShareTexteFragment("https://www.tjo.com/"+texte.NumSGG) ;
            val activity = it.context as AppCompatActivity
            dialog.show(activity.supportFragmentManager , "ShareTexteFragment" )
        }


    }




    override fun onDestroyView() {
        super.onDestroyView()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
       /* if (requestCode == TextePageFragment.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception=task.exception
            if(task.isSuccessful)
            {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    // Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    print( "COMPTE :" + account.id.toString())

                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    // Log.w("SignInActivity", "Google sign in failed", e)
                    print("SignInActivity "+ "GOOGLE MAFIHAACH"+ e.toString())
                }}else{
                //Log.w("SignInActivity", exception.toString())
                print("SignInActivity "+ exception.toString())

            }


        }
        */
    }





}