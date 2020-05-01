package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        var handler = Handler()
        handler.postDelayed(
            Runnable {
                run{
                    if (mAuth?.currentUser != null) {
                        intent = Intent(this, HomePage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                }
            }
            ,2500)

    }
}
