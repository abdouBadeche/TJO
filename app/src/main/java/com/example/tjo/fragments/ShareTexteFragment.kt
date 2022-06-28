package com.example.tjo.fragments

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tjo.Entitties.Contact
import com.example.tjo.Entitties.Texte
import com.example.tjo.R
import kotlinx.android.synthetic.main.filter_fragement.view.*
import kotlinx.android.synthetic.main.share_texte_fragment.*
import kotlinx.android.synthetic.main.share_texte_fragment.view.*


class ShareTexteFragment(var url: String)  : DialogFragment() {

    private var contactModelArrayList: ArrayList<Contact>? = null

    var contact = "" ;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View  = inflater.inflate(R.layout.share_texte_fragment , container  , false)
        val contentResolver = requireActivity().contentResolver


        var mPreferences: SharedPreferences = rootView.context.applicationContext.getSharedPreferences("LANG",
            Context.MODE_PRIVATE
        )
        var lang = mPreferences.getString("LANG", "AR")



        if(lang.equals("FR")) {
            rootView.textView15.text = "Partager le Texte par Mail"
        }else {
            rootView.textView15.text = "مشاركة النص عن طريق البريد"


        }

        val contacts = ShowContact(contentResolver)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item,contacts!!.toArray())

        val dropList = rootView.findViewById<Spinner>(R.id.ListContact)

        dropList!!.adapter = arrayAdapter


        dropList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                contact = dropList.selectedItem.toString() ;

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        rootView.findViewById<ImageView>(R.id.btn_send).setOnClickListener {
            sendEmail(contact , "" , url) ;
        }

        return rootView
    }





    fun ShowContact(resolver: ContentResolver) : ArrayList<String>? {
        val nameList = ArrayList<String>()
        val phoneList = ArrayList<String>()
        val emailList = ArrayList<String>()

        val contacts = ArrayList<Contact>() ;

        val cr: ContentResolver = resolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            null, null, null
        )
        if (cur!!.getCount() > 0) {
            while (cur!!.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur
                    .getString(
                        cur
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    )
                if (cur.getString(
                        cur
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    ).toInt() > 0
                ) {
                    // Query phone here. Covered next
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = ?", arrayOf(id), null
                    )
                    while (pCur!!.moveToNext()) {
                        // Do something with phones
                        val phoneNo: String = pCur
                            .getString(
                                pCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                        nameList.add(name) // Here you can list of contact.
                        phoneList.add(phoneNo) // Here you will get list of phone number.
                        val emailCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (emailCur!!.moveToNext()) {
                            val email: String =
                                emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            emailList.add(email) // Here you will get list of email
                            contacts.add(Contact(email)) ;
                        }
                        emailCur.close()
                    }
                    pCur.close()
                }
            }
        }


        return emailList
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){

        }

    }


}