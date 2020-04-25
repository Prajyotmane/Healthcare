package com.prajyotmane.healthcare

import android.os.Bundle
import android.util.Log
import android.view.View
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
        var name = userName.text.trim().toString()
        var email = userEmail.text.trim().toString()
        var contact = userContact.text.trim().toString()

        var password = userPassword.text.toString()
        var confpassword = userConfPassword.text.toString()

        if (name.isEmpty()) {
            userName.setError("Name cannot be empty")
            return
        }
        if (email.isEmpty()) {
            userEmail.setError("Email cannot be empty")
            return
        }
        if (contact.isEmpty()) {
            userContact.setError("Contact number cannot be enpty")
            return
        }
        if (password.isEmpty()) {
            userPassword.setError("Password cannot be enpty")
            return
        }
        if (confpassword.isEmpty()) {
            userConfPassword.setError("Please enter password to confirm")
            return
        }
        if (!password.isEmpty() && !confpassword.isEmpty() && !password.equals(confpassword)) {
            userPassword.setError("Password and confirm password should match")
            Log.d("test",password+ " " +confpassword)
            return
        }
        mAuth?.createUserWithEmailAndPassword(email.toString(), password.toString())?.addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                // Create a new user with a first and last name
                var user = mapOf<String,String>(
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
                }?.addOnFailureListener { e ->
                    Log.d("UserRegistration", "Error adding document", e)
                }
                } else {
                // If sign in fails, display a message to the user.
                Log.d("UserRegistration", "Authenticatiion Task failed"+" "+task.exception)

            }
        }

    }
}
