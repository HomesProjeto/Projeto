package com.br.homes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.br.homes.Util.userUtil
import com.br.homes.model.Users
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        if (userUtil.uid.equals("")) {
            initialize()
            Picasso.get().load(userUtil.profileImageUrl).resize(90, 90).centerCrop().into(imageUser)
            nomeUser.text = userUtil.name
            emailUser.text = userUtil.email
        } else {

            Picasso.get().load(userUtil.profileImageUrl).resize(90, 90).centerCrop().into(imageUser)
            nomeUser.text = userUtil.name
            emailUser.text = userUtil.email
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun initialize() {
        val userID = auth.currentUser!!.uid
        val firebaseDatabase = FirebaseDatabase.getInstance()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout) {
            auth.signOut()
            val i = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

}
