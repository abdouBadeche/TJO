package com.example.tjo.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tjo.R
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection



var pdfView: PDFView? = null

class TextePdfFragment(val url:String) : Fragment(R.layout.texte_pdf_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pdfView = view.findViewById<PDFView>(R.id.idPDFView)

        RetrivePDFfromUrl().execute(url)
    }

    // create an async task class for loading pdf file from URL.

   internal class RetrivePDFfromUrl :
        AsyncTask<String?, Void?, InputStream?>() {


        override fun doInBackground(vararg params: String?): InputStream? {

            // we are using inputstream
            // for getting out PDF.
            var inputStream: InputStream? = null
            try {
                val url = URL(params[0])
                // below is the step where we are
                // creating our connection.
                val urlConnection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.getResponseCode() === 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = BufferedInputStream(urlConnection.getInputStream())
                }
            } catch (e: IOException) {
                // this is the method
                // to handle errors.
                e.printStackTrace()
                return null
            }
            return inputStream
        }


        override fun onPostExecute(inputStream: InputStream?) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.

            pdfView?.fromStream(inputStream)?.load();
        }

    }
}