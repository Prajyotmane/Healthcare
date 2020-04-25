package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        if(mAuth?.currentUser!=null){
            startActivity(Intent(this,HomePage::class.java))
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}
