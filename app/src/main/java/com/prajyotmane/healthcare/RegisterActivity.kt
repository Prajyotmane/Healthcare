package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    var db: FirebaseFirestore? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()
    }

    fun registerUser(view: View) {
        register_progress.setVisibility(View.VISIBLE)
        var name = userName.text.trim().toString()
        var email = userEmail.text.trim().toString()
        var contact = userContact.text.trim().toString()

        var password = userPassword.text.toString()
        var confpassword = userConfPassword.text.toString()

        if (name.isEmpty()) {
            userName.setError("Name cannot be empty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (email.isEmpty()) {
            userEmail.setError("Email cannot be empty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (contact.isEmpty()) {
            userContact.setError("Contact number cannot be enpty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (password.isEmpty()) {
            userPassword.setError("Password cannot be enpty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (confpassword.isEmpty()) {
            userConfPassword.setError("Please enter password to confirm")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (!password.isEmpty() && !confpassword.isEmpty() && !password.equals(confpassword)) {
            userPassword.setError("Password and confirm password should match")
            register_progress.setVisibility(View.GONE)
            return
        }
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Create a new user with a first and last name
                    var user = mapOf<String, String>(
                        "Name" to name,
                        "Email" to email,
                        "Contact" to contact
                    )

// Add a new document with a generated ID
                    db?.collection("Users")?.add(user)?.addOnSuccessListener { documentReference ->
                        Log.d(
                            "UserRegistration",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                        Toast.makeText(this, "Registration was successful!", Toast.LENGTH_SHORT)
                            .show()
                        intent = Intent(this, HomePage::class.java)
                        register_progress.visibility = View.GONE
                        this.finish()
                        startActivity(intent)

                    }?.addOnFailureListener { e ->
                        Log.d("UserRegistration", "Error adding document", e)
                        Toast.makeText(
                            this,
                            "An error occured. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("UserRegistration", "Authenticatiion Task failed" + " " + task.exception)
                    Toast.makeText(
                        this,
                        "An account with email: " + email + " already exist.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        register_progress.setVisibility(View.GONE)

    }

    fun goToLoginPage(view: View) {
        intent = Intent(this, LoginActivity::class.java)
        this.finish()
        startActivity(intent)
    }
}
