package com.example.homes.ui.tools

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.br.homes.PropertyProfileActivity
import com.br.homes.Util.myhomes
import com.br.homes.Util.myhomesUtil
import com.br.homes.Util.propertyUtilAll
import com.br.homes.adapter.ListMyProperty
import com.br.homes.model.Property
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_myhomes.*


class MyHomesFragment : Fragment() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(com.br.homes.R.layout.fragment_myhomes, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userID = auth.currentUser!!.uid
        val ref = firebaseDatabase.getReference("PropertyUser/").child(userID)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                myhomesUtil.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    myhomesUtil.add(property)
                    propertyUtilAll.add(property)
                    println(property.url)
                }

                val list = ListMyProperty(context!!)
                list.addAll(myhomesUtil)
                list_myproperty.adapter = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar suas casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)



        list_myproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            myhomes = true
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        myhomes = false
    }
}