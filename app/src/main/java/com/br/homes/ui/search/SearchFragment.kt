package com.example.homes.ui.search

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.br.homes.PropertyProfileActivity
import com.br.homes.Util.*
import com.br.homes.adapter.ListMyProperty
import com.br.homes.model.Property
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import ir.apend.slider.model.Slide
import kotlinx.android.synthetic.main.activity_property_registration.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    lateinit var position_p: String

    lateinit var spinner: Spinner
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(com.br.homes.R.layout.fragment_search, container, false)

        val array =
            arrayOf(
                "Todos os imóveis",
                "Casas",
                "Casas para alugar",
                "Casas para vender",
                "Apartamentos",
                "Apartamentos para alugar",
                "Apartamentos para vender"
            )

        val adapter = ArrayAdapter(
            activity!!.applicationContext,
            R.layout.simple_spinner_dropdown_item,
            array
        )

        spinner = root.findViewById(com.br.homes.R.id.search)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)


        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>, view: View,
                    position: Int, id: Long
                ) {
                    if (position == 0) {
                        propertyUtilAll.clear()
                        all()
                    } else if (position == 1) {
                        propertyUtilAll.clear()
                        casas()
                    } else if (position == 2) {
                        propertyUtilAll.clear()
                        casasAlugar()
                    } else if (position == 3) {
                        propertyUtilAll.clear()
                        casasVender()
                    } else if (position == 4) {
                        propertyUtilAll.clear()
                        apartamento()
                    } else if (position == 5) {
                        propertyUtilAll.clear()
                        apartamentoAlugar()
                    } else if (position == 6) {
                        propertyUtilAll.clear()
                        apartamentoVender()
                    }

                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    // TODO Auto-generated method stub
                }

            }

        return root
    }

    fun all() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    propertyUtilAll.add(property)
                    println(property.url)
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun casas() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Casa comum") {
                        propertyUtilAll.add(property)
                        println(property.url)
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun casasAlugar() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Casa comum") {
                        if (property.option == "Alugar") {
                            propertyUtilAll.add(property)
                            println(property.url)
                        }
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun casasVender() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Casa comum") {
                        if (property.option == "Vender") {
                            propertyUtilAll.add(property)
                            println(property.url)
                        }
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun apartamento() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Apartamento") {
                        propertyUtilAll.add(property)
                        println(property.url)
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun apartamentoAlugar() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Apartamento") {
                        if (property.option == "Alugar") {
                            propertyUtilAll.add(property)
                            println(property.url)
                        }
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    fun apartamentoVender() {
        myhomes = false
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    if (property.type == "Apartamento" && property.option == "Vender") {
                            propertyUtilAll.add(property)
                            println(property.url)
                    }
                }
                val list = ListMyProperty(context!!)
                list.addAll(propertyUtilAll)
                list_allproperty.adapter = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)

        list_allproperty.setOnItemClickListener { parent, view, position, id ->
            val property = propertyUtilAll.get(position)
            phoneUtil = property.phone!!
            propertyUtilAll.clear()
            propertyUtilAll.add(property)
            val i = Intent(context, PropertyProfileActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        spinner.setSelection(0)
        all()
    }
}
