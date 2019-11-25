package com.br.homes

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        setSupportActionBar(toolbar_reset)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        val firebaseAuth = FirebaseAuth.getInstance()
        button_reset.setOnClickListener {

            val progressDialog = ProgressDialog.show(this,null,null);
            progressDialog.setContentView(R.layout.loader)
            progressDialog.window!!.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
            progressDialog.setCancelable(false)

            if (text_email.getText().toString() != "") {

                firebaseAuth
                    .sendPasswordResetEmail(text_email.getText().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            AlertDialog.Builder(this)
                                .setTitle("Email de recuperação de senha")
                                .setMessage("Enviamos um email de redefinição para o email informado")
                                .setPositiveButton("Ok", null)
                                .show()
                        } else {
                            progressDialog.dismiss()
                            AlertDialog.Builder(this)
                                .setTitle("Email inválido")
                                .setMessage("O email informado é inválido")
                                .setPositiveButton("Ok", null)
                                .show()
                        }
                    }

            } else {
                progressDialog.dismiss()
                AlertDialog.Builder(this)
                    .setTitle("Email inválido")
                    .setMessage("O email informado é inválido")
                    .setPositiveButton("Ok", null)
                    .show()
            }
        }

    }
}
