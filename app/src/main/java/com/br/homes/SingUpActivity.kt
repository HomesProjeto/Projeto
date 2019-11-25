package com.br.homes

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.homes.model.Users
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.io.ByteArrayOutputStream
import java.util.*

class SingUpActivity : AppCompatActivity() {

    var text_name: EditText? = null
    var text_email: EditText? = null
    var text_password: EditText? = null
    var text_confirm_password: EditText? = null
    val user = Users()
    val random_uid = UUID.randomUUID().toString()
    var selectPhotoUri: Uri? = null

    var auth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        txt_msg.alpha = 0f
        val msg = findViewById(R.id.txt_msg) as TextView

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val i = Intent(
                this@SingUpActivity,
                SplashActivity::class.java
            )
            finish()
            startActivity(i)
        }
        inicializeFirebase()
        initializeComponents()

        val enter_button = findViewById(R.id.enter_button) as TextView
        enter_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val i = Intent(this@SingUpActivity, LoginActivity::class.java)
                startActivity(i)
            }
        })

        button_cadastro.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (text_password!!.text.toString().equals(text_confirm_password!!.text.toString()) || text_password!!.text.toString() == "" || text_confirm_password!!.text.toString() == "") {
                    if (text_email!!.text.toString() == "") {
                        msg.text = "Email inválido"
                        txt_msg.alpha = 1f
                    } else if (text_password!!.text.toString() == "") {
                        msg.text = "Senha inválida"
                        txt_msg.alpha = 1f
                    } else if (text_password!!.length() <= 5) {
                        msg.text = "Senha inválida"
                        txt_msg.alpha = 1f
                    } else if(selectPhotoUri == null) {
                        msg.text = "Insira uma foto de perfil"
                        txt_msg.alpha = 1f
                    } else {
                        newRegister()
                    }
                } else {
                    msg.text = "Senhas não coincidem"
                    txt_msg.alpha = 1f
                }
            }
        })
        val pick = pick

        pick.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, 0)
        }
    }

    private fun newRegister() {

        val progressDialog = ProgressDialog.show(this,null,null);
        progressDialog.setContentView(R.layout.loader)
        progressDialog.window!!.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        progressDialog.setCancelable(false)

        auth.createUserWithEmailAndPassword(
            text_email!!.text.toString(),
            text_password!!.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    uploadImageToFirebase()
                    progressDialog.dismiss()
                    val i = Intent(
                        this@SingUpActivity,
                        PropertyRegistrationActivity::class.java
                    )
                    finish()
                    startActivity(i)
                } else {
                    progressDialog.dismiss()
                    txt_msg.text = "Email já cadastrado"
                    txt_msg.alpha = 1f
                }
            }
    }
    private fun inicializeFirebase() {
        FirebaseApp.initializeApp(this@SingUpActivity)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
    }

    fun newUser(profileImageUrl: String) {
        var authID = auth.uid
        user.uid = random_uid
        user.email = text_email!!.text.toString()
        user.name = text_name!!.text.toString()
        user.profileImageUrl = profileImageUrl
        user.password = text_password!!.text.toString()
        databaseReference!!.child("Users/").child(authID.toString()).setValue(user)
    }

    fun initializeComponents() {
        text_name = findViewById(R.id.text_name) as EditText
        text_email = findViewById(R.id.text_email) as EditText
        text_password = findViewById(R.id.text_password) as EditText
        text_confirm_password = findViewById(R.id.text_confirm_password) as EditText
    }

    private fun alert(msg: String) {
        Toast.makeText(this@SingUpActivity, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)

            val baos = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            pick.setImageBitmap(bitmap)

            val textView = textView

            textView.alpha = 0f

        }
    }

    fun uploadImageToFirebase() {
        val authID = auth.uid.toString()
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val ref = storageReference.child("/profiles/$authID")
        if(selectPhotoUri != null) {
            ref.putFile(selectPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            newUser(downloadUri.toString())
                        }
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro no cadastro!", Toast.LENGTH_SHORT).show()
                        }
                }
        } else {
            txt_msg.text = "Insira uma foto de perfil"
            txt_msg.alpha = 1f
        }

    }
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null) {
            val i = Intent (
                this@SingUpActivity, MainActivity::class.java
            )
            startActivity(i)
        }
    }
}
