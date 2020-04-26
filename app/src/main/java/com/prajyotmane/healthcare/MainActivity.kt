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
        if (mAuth?.currentUser != null) {
            intent = Intent(this, HomePage::class.java)
            this.finish()
            startActivity(intent)
        } else {
            intent = Intent(this, LoginActivity::class.java)
            this.finish()
            startActivity(intent)
        }
    }
}
