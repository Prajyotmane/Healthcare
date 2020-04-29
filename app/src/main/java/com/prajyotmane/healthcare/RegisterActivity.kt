package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.remote.Datastore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    lateinit var db: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        db = FirebaseDatabase.getInstance().getReference("users")
        mAuth = FirebaseAuth.getInstance()
    }

    fun registerUser(view: View) {
        register_progress.setVisibility(View.VISIBLE)
        var fname = userFirstName.text.trim().toString()
        var lname = userLastName.text.trim().toString()
        var email = userEmail.text.trim().toString()
        var contact = userContact.text.trim().toString()

        var password = userPassword.text.toString()
        var confpassword = userConfPassword.text.toString()

        if (fname.isEmpty()) {
            userFirstName.setError("First Name cannot be empty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (lname.isEmpty()) {
            userLastName.setError("Last Name cannot be empty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if (email.isEmpty()) {
            userEmail.setError("Email cannot be empty")
            register_progress.setVisibility(View.GONE)
            return
        }
        if(!email.isEmpty() and !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Email address is invalid")
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
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                // Create a new user with a first and last name
                var user = UserDataClass(fname, lname, email, contact)

                var uID = mAuth.currentUser!!.uid
                db.child(uID).setValue(user)
                Toast.makeText(
                    this,
                    "Registration was successful!",
                    Toast.LENGTH_SHORT
                ).show()
                var intent = Intent(this, HomePage::class.java)
                this.finish()
                startActivity(intent)

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
