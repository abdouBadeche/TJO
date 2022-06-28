package com.example.tjo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tjo.MainActivity
import com.example.tjo.R
import kotlinx.android.synthetic.main.splash_screen.*


class SplashScreenActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        button.setOnClickListener{

            val prms = getSharedPreferences("LANG", 0)
            val editeur = prms.edit()
            editeur.putString("LANG" , "FR" ) ;
            editeur.commit()


            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        button_ar.setOnClickListener{

            val prms = getSharedPreferences("LANG", 0)
            val editeur = prms.edit()
            editeur.putString("LANG" , "AR" ) ;
            editeur.commit()


            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}