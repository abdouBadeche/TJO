package com.example.tjo


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tjo.Adapter.TexteAdapter
import com.example.tjo.Entities.ElasticSearchAPI
import com.example.tjo.Entities.HitsList
import com.example.tjo.Entities.HitsObject
import com.example.tjo.Entitties.Texte
import com.example.tjo.activities.SplashScreenActivity
import com.example.tjo.fragments.TexteFavFragment
import com.example.tjo.fragments.TextePageFragment
import com.example.tjo.fragments.TexteSearchedFragment
import com.example.tjo.fragments.TextesFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    var textes : List<Texte> = emptyList() ;


    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    //contact permission code
    private val CONTACT_PERMISSION_CODE = 1;
    //contact pick code
    private val CONTACT_PICK_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the data from our
        // intent in our uri.

        // getting the data from our
        // intent in our uri.
        val uri: Uri? = intent.data

        // checking if the uri is null or not.

        // checking if the uri is null or not.
        if (uri != null) {

            val parameters: List<String> = uri.getPathSegments()

            val param = parameters[0] ;

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

            if (!param.equals("")) {
                searchString = "$searchString NumSGG:$param"
            }


            if (searchString.equals("")) {
                searchString = "*"
            }


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




                        var textes = emptyList<Texte>() ;
                        textes = emptyList() ;
                        for (i in hitsList!!.postIndex!!.indices) {
                            Log.d("data", "onResponse: data: " + hitsList!!.postIndex!![i].post.toString())
                            textes += hitsList!!.postIndex!![i].post as Texte ;
                        }

                        val textFragmenet = TextePageFragment( textes.first() ) ;


                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.flFragment,  textFragmenet)
                            commit()
                        }

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

        //check permission allowed or not
        if (checkContactPermission()){
            //allowed
        }
        else{
            //not allowed, request
            requestContactPermission()
        }

        val drawerLayout: DrawerLayout = drawer_layout
        val navView: NavigationView = nav_view

        if (navView != null) {
            navView.setNavigationItemSelectedListener(this);
        }

        menu_btn.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }


        val params = getSharedPreferences("LANG", 0)
        val lang = params.getString("LANG" , "FR").toString()
        Log.w("lang" , lang) ;

        if(lang.equals("FR")) {
            navView.menu.findItem(R.id.nav_search).title = "Trouver un texte" ;
            navView.menu.findItem(R.id.nav_history).title = "Historiques" ;
            navView.menu.findItem(R.id.nav_fav).title = "Favs" ;
            navView.menu.findItem(R.id.nav_params).title = "Paramètres" ;
        }else {
            navView.menu.findItem(R.id.nav_search).title = "البحث" ;
            navView.menu.findItem(R.id.nav_history).title = "سجل البحث" ;
            navView.menu.findItem(R.id.nav_fav).title = "المفضبة" ;
            navView.menu.findItem(R.id.nav_params).title = "الإعدادات" ;
        }
/*
        val jsonFileString = getJsonDataFromAsset(applicationContext, "Modele_JO_TDM.json")
        Log.i("data", jsonFileString.toString())
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Texte>>() {}.type
        textes = gson.fromJson(jsonFileString, listPersonType) ;

*/


        val racineFragment = TextesFragment(textes!!)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,  racineFragment)
            commit()
        }




    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.nav_search -> {

                val texteFragment = TextesFragment(textes!!)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment,  texteFragment)
                    commit()
                }

                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()

            }

            R.id.nav_history -> {

                val texteSearchedFragment = TexteSearchedFragment()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment,  texteSearchedFragment)
                    commit()
                }


                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_fav -> {
              val versetFavFragment = TexteFavFragment()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment,  versetFavFragment)
                    commit()
                }
                Toast.makeText(this, "Favories", Toast.LENGTH_SHORT).show()


            }

            R.id.nav_params -> {
                val intent= Intent(this,SplashScreenActivity::class.java)
                startActivity(intent)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun checkContactPermission(): Boolean{
        //check if permission was granted/allowed or not, returns true if granted/allowed, false if not
        return  ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission(){
        //request the READ_CONTACTS permission
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS )
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //handle permission request results || calls when user from Permission request dialog presses Allow or Deny
        if (requestCode == CONTACT_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted, can pick contact
            }
            else{
                //permission denied, cann't pick contact, just show message
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }
        }
    }


}