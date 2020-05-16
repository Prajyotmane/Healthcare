package com.prajyotmane.healthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)
    }

    fun onDoctorLoginClick(view: View) {
        startActivity(Intent(this, DoctorLogin::class.java))
        finish()
    }
    fun onUserLoginClick(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    fun onUserSignUpClick(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
    fun onDoctorSignupClick(view: View) {
        startActivity(Intent(this, DoctorRegistration::class.java))
        finish()
    }
}
