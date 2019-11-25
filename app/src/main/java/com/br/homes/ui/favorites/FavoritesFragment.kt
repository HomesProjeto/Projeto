package com.example.homes.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.br.homes.PropertyProfileActivity
import com.br.homes.R
import com.br.homes.Util.favoriteUtil
import com.br.homes.Util.myhomes
import com.br.homes.Util.propertyUtilAll
import com.br.homes.adapter.ListMyProperty
import com.br.homes.model.Property
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_search.*

class FavoritesFragment : Fragment() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        favoriteUtil.clear()
        return root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        myhomes = false
        val userID = auth.currentUser!!.uid
        val mContext = context!!
        val list = ListMyProperty(mContext)
        favoriteUtil.clear()
        propertyUtilAll.clear()
        val ref = firebaseDatabase.getReference("StarProperty/").child(userID)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    for (snap in postSnapshot.children) {
                        val property = snap.getValue(Property::class.java) as Property
                        list.add(property)
                        propertyUtilAll.add(property)
                        println(property.url)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro!!", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)
        list_favorite.adapter = list


        list_favorite.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }
}
