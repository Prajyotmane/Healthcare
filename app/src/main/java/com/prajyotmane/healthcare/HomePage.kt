package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth?.currentUser
        nav_view.setNavigationItemSelectedListener(this)

        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DefaultHomePage()).commit()
    }



    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            val count = supportFragmentManager.backStackEntryCount
            if (count == 0) {
                super.onBackPressed()
                //additional code
            } else {
                supportFragmentManager.popBackStack()
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_Home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DefaultHomePage()).commit()
            R.id.nav_profile -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, UserProfile()).commit()
            R.id.nav_logout -> {
                mAuth?.signOut()
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

