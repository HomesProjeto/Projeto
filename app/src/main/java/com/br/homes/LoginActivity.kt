package com.br.homes

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var text_email: EditText? = null
    var text_password: EditText? = null
    var button: Button? = null
    var auth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference? = null
    var REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txt_error.alpha = 0f
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE
            )
        }

        forgot_password.setOnClickListener {
            val i = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(i)
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val i = Intent(
                this@LoginActivity,
                SplashActivity::class.java
            )
            finish()
            startActivity(i)
        }

        text_email = findViewById(R.id.text_email) as EditText
        text_password = findViewById(R.id.text_password) as EditText
        button = findViewById(R.id.button_reset)
        val enter_button = findViewById(R.id.enter_button) as TextView

        enter_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(this@LoginActivity, SingUpActivity::class.java)
                startActivity(i)
            }
        }

        )

        button!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (text_email!!.text.toString() != "" && text_password!!.text.toString() != "") {
                    txt_error.alpha = 0f
                    val txt_email = findViewById<TextView>(R.id.text_email)
                    val txt_password = findViewById<TextView>(R.id.text_password)
                    txt_email.setBackgroundResource(R.drawable.border)
                    txt_password.setBackgroundResource(R.drawable.border)
                    inicializeFirebase()
                    newLogin()
                } else {
                    txt_error.alpha = 1f
                    val txt_email = findViewById<TextView>(R.id.text_email)
                    val txt_password = findViewById<TextView>(R.id.text_password)
                    txt_email.setBackgroundResource(R.drawable.borderred)
                    txt_password.setBackgroundResource(R.drawable.borderred)
                }
            }
        })
    }

    private fun newLogin() {
        /*val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.show()
         */

        val progressDialog = ProgressDialog.show(this, null, null);
        progressDialog.setContentView(R.layout.loader)
        progressDialog.window!!.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        progressDialog.setCancelable(false)

        auth.signInWithEmailAndPassword(
            text_email!!.text.toString(),
            text_password!!.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    val i = Intent(
                        this@LoginActivity,
                        SplashActivity::class.java
                    )
                    finish()
                    startActivity(i)
                } else {
                    progressDialog.dismiss()
                    txt_error.alpha = 1f
                    val txt_email = findViewById<TextView>(R.id.text_email)
                    val txt_password = findViewById<TextView>(R.id.text_password)
                    txt_email.setBackgroundResource(R.drawable.borderred)
                    txt_password.setBackgroundResource(R.drawable.borderred)
                }
            }
    }

    private fun inicializeFirebase() {
        FirebaseApp.initializeApp(this@LoginActivity)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
    }

    private fun alert(msg: String) {
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
    }
}