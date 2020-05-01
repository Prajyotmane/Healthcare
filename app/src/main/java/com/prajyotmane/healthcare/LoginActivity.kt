package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var mAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }
    fun loginUser(view:View){
        var email = emailId.text.trim().toString()
        var password = passwd.text.trim().toString()

        if (email.isEmpty()) {
            emailId.setError("Email cannot be empty")
            return
        }
        if (password.isEmpty()) {
            passwd.setError("Password cannot be empty")
            return
        }


        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,HomePage::class.java))
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Invalid Email ID or Password", Toast.LENGTH_SHORT).show()
                }

            }
}
    fun registerUser(view:View){
        intent = Intent(this,RegisterActivity::class.java)
        finish()
        startActivity(intent)    }
}
