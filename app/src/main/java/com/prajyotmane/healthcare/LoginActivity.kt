package com.prajyotmane.healthcare

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    private lateinit var storedData: SharedPreferences
    lateinit var loading: LoadingDialogBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        storedData = PreferenceManager.getDefaultSharedPreferences(this)
        loading = LoadingDialogBox(this)

    }

    fun loginUser(view: View) {

        loading.startLoading()
        var email = emailId.text.trim().toString()
        var password = passwd.text.trim().toString()

        if (email.isEmpty()) {
            emailId.setError("Email cannot be empty")
            loading.cancelLoading()
            return
        }
        if (password.isEmpty()) {
            passwd.setError("Password cannot be empty")
            loading.cancelLoading()

            return
        }


        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyAuthenticity()
                } else {
                    loading.cancelLoading()
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Invalid Email ID or Password", Toast.LENGTH_SHORT).show()
                }

            }
    }

    fun verifyAuthenticity() {
        val user = mAuth?.currentUser
        val uID = user!!.uid
        var db = FirebaseDatabase.getInstance().getReference("users")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var flag =
                    dataSnapshot.child(uID).child("email").getValue()
                if (flag == null) {
                    loading.cancelLoading()
                    Toast.makeText(
                        baseContext, "Login failed, Please log in as a Doctor.",
                        Toast.LENGTH_LONG
                    ).show()
                    mAuth?.signOut()
                } else {
                    val storeData = storedData.edit()
                    storeData.putString("Role", "1")
                    storeData.commit()
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    loading.cancelLoading()
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)
    }

    fun registerUser(view: View) {
        intent = Intent(this, RegisterActivity::class.java)
        finish()
        startActivity(intent)
    }
}
