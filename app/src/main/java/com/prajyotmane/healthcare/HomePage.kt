package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomePage : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        mAuth = FirebaseAuth.getInstance()
    }
    fun logOut(view:View){
        mAuth?.signOut()
        startActivity(Intent(this,MainActivity::class.java))
    }
}
