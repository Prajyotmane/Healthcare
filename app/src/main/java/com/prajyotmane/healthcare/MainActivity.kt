package com.prajyotmane.healthcare

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    private lateinit var storedData: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        storedData = PreferenceManager.getDefaultSharedPreferences(this)
        var role = storedData.getString("Role", null)

        var handler = Handler()
        handler.postDelayed(
            Runnable {
                run{
                    if (mAuth?.currentUser != null) {
                        if(role=="1"){
                            intent = Intent(this, HomePage::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            intent = Intent(this, DoctorNavigationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        intent = Intent(this, WelcomePage::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            ,2500)

    }
}
