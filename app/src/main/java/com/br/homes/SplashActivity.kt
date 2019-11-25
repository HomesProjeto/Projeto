package com.br.homes

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.br.homes.Util.userUtil
import com.br.homes.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    var SPLASH_TIME_OUT: Long = 3000
    lateinit var i: Intent
    var auth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            i = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            initialize()
        } else {
            i = Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
        }
        Handler().postDelayed(
            Runnable()
            {
                finish()
                startActivity(i)
            }, SPLASH_TIME_OUT
        )
    }
    fun initialize() {
        val userID = auth.currentUser!!.uid
        val ref = firebaseDatabase.getReference("Users/").child(userID)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = dataSnapshot.getValue(Users::class.java) as Users
                userUtil.email = users.email
                userUtil.name = users.name
                userUtil.uid = users.uid
                userUtil.profileImageUrl = users.profileImageUrl
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        ref.addValueEventListener(postListener)
    }
}

