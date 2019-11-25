package com.br.homes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.br.homes.model.Property
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_property_profile.*
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.br.homes.Util.*
import ir.apend.slider.model.Slide
import kotlinx.android.synthetic.main.activity_property_profile.image
import kotlinx.android.synthetic.main.activity_property_profile.spinner
import kotlinx.android.synthetic.main.activity_property_profile.toolbar
import kotlinx.android.synthetic.main.activity_property_profile.transacao
import kotlinx.android.synthetic.main.activity_property_profile.txt_address
import kotlinx.android.synthetic.main.activity_property_profile.txt_burgh
import kotlinx.android.synthetic.main.activity_property_profile.txt_cep
import kotlinx.android.synthetic.main.activity_property_profile.txt_complement
import kotlinx.android.synthetic.main.activity_property_profile.txt_complement2
import kotlinx.android.synthetic.main.activity_property_profile.txt_description
import kotlinx.android.synthetic.main.activity_property_profile.txt_phone
import kotlinx.android.synthetic.main.activity_property_profile.txt_price


class PropertyProfileActivity : AppCompatActivity() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()
    val authID = FirebaseAuth.getInstance().currentUser!!.uid
    val ref = firebaseDatabase.getReference("StarProperty/").child(authID)
    val refImage = firebaseDatabase.getReference("Imagens/").child(propertyUtilAll.get(0).random!!)
    var favoriteBoolean = false
    lateinit var phoneGlobal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.br.homes.R.layout.activity_property_profile)

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        if(myhomes) {
            send_button.text = "Editar Imóvel"
            send_button.setOnClickListener {
                val i = Intent(this, EditActivity::class.java)
                startActivity(i)
            }

        } else {
            send_button.setOnClickListener {
                enviarWhatsApp("Olá! Vi seu anuncio no Homes e me interessei pela sua casa! \n Localização: ${propertyUtilAll.get(0).address}, ${propertyUtilAll.get(0).burgh}, ${propertyUtilAll.get(0).cep}, ${propertyUtilAll.get(0).description}\n\n Gostaria de mais informações a respeito do imóvel!")
            }
        }

        initialize()



        skipP.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            propertyUtilAll.clear()
            propertyUtil.clear()
            profileUtil.clear()
            finish()
            startActivity(i)
        }

        star_view.setOnClickListener {
            if(favoriteBoolean) {
                star_view.setBackground(ContextCompat.getDrawable(this, com.br.homes.R.drawable.ic_star_empty))
                ref.child(propertyUtilAll.get(0).random!!).removeValue()
                favoriteBoolean = false
            }
            else{
                ref.child(propertyUtilAll.get(0).random!!).setValue(propertyUtilAll)
                star_view.setBackground(ContextCompat.getDrawable(this, com.br.homes.R.drawable.ic_star_fill))
                favoriteBoolean = true
            }
        }
    }

    fun initialize() {
        val slider = image

        val slideList = arrayListOf<Slide>()
        slideList.add(
            Slide(
                0,
                propertyUtilAll.get(0).url,
                resources.getDimensionPixelSize(com.br.homes.R.dimen.slider_image_corner)
            )
        )

        slider.addSlides(slideList)



        txt_phone.text = propertyUtilAll.get(0).phone
        txt_price.text = propertyUtilAll.get(0).price
        txt_complement2.text = propertyUtilAll.get(0).number
        spinner.text = propertyUtilAll.get(0).type
        transacao.text = propertyUtilAll.get(0).option
        txt_address.text = propertyUtilAll.get(0).address
        txt_burgh.text = propertyUtilAll.get(0).burgh
        txt_cep.text = propertyUtilAll.get(0).cep
        txt_description.text = propertyUtilAll.get(0).description
        txt_complement.text = propertyUtilAll.get(0).complement

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if(!property.random.equals("")) {
                        favoriteBoolean = true
                        star_view.setBackground(ContextCompat.getDrawable(this@PropertyProfileActivity, com.br.homes.R.drawable.ic_star_fill))
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@PropertyProfileActivity, "Error!!", Toast.LENGTH_SHORT).show()
            }
        }
        ref.child(propertyUtilAll.get(0).random!!).addListenerForSingleValueEvent(postListener)
    }

    fun enviarWhatsApp(mensagem: String) {
        try {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.action = Intent.ACTION_VIEW
            sendIntent.setPackage("com.whatsapp")
           val phone = phoneUtil
            val url =
                "https://api.whatsapp.com/send?phone=+55$phone&text=" + "$mensagem"
            Log.d("cezargay",phone)
            sendIntent.data = Uri.parse(url)
            if (sendIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivity(sendIntent)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show()
        }

    }

}
