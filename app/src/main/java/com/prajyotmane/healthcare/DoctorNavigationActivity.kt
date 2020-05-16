package com.prajyotmane.healthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_doctor_navigation.*

class DoctorNavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_navigation)

        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()

        nav_view.setNavigationItemSelectedListener(this)

        var toggle:ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout,toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DoctorHomeFragment()).commit()
            nav_view.setCheckedItem(R.id.nav_doc_home)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nav_doc_home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DoctorHomeFragment()).commit()
            R.id.nav_update -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DoctorProfileFragment()).commit()
            //R.id.nav_appointments -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DoctorAppointmentFragment()).commit()

            R.id.nav_appointments -> {
                var intent = Intent(this,DoctorAppointment::class.java)
                startActivity(intent)
            }
            R.id.nav_logout-> {
                mAuth.signOut()
                var intent = Intent(this,WelcomePage::class.java)
                startActivity(intent)
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }

    }
}
