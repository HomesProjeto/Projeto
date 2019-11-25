package com.example.homes.ui.send

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.InetAddresses
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.br.homes.MainActivity
import com.br.homes.PropertyRegistrationActivity
import com.br.homes.R
import com.br.homes.SplashActivity
import com.br.homes.Util.myhomes
import com.br.homes.Util.returnActivity
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_property_registration.*
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.CEP
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.RetrofitInitializer
import java.io.ByteArrayOutputStream
import java.util.*

class AddFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(com.br.homes.R.layout.loader, container, false)
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = Intent(context, PropertyRegistrationActivity::class.java)
        returnActivity = true
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        myhomes = false
    }
}
