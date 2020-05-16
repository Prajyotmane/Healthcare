package com.prajyotmane.healthcare

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_doctor_login.*
import kotlinx.android.synthetic.main.activity_doctor_registration.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DoctorLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storedData: SharedPreferences
    private lateinit var loading: LoadingDialogBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_login)
        loading = LoadingDialogBox(this)
        auth = FirebaseAuth.getInstance()
        storedData = PreferenceManager.getDefaultSharedPreferences(this)

        val user = auth.currentUser
        if (user != null) {
            updateUI(user)
        }

        docLoginButton.setOnClickListener {
            loading.startLoading()
            doctorLogin()
        }

        loginRegButton.setOnClickListener {
            startActivity(Intent(this, DoctorRegistration::class.java))
        }
    }

    private fun doctorLogin() {

        val docEmail = loginEmailText.text.toString()
        val docPassword = loginPasswordText.text.toString()

        if (docEmail.isEmpty()) {
            loginEmailText.error = "Please enter email"
            loginEmailText.requestFocus()
            loading.cancelLoading()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(docEmail).matches()) {
            loginEmailText.error = "Please enter a valid email"
            loading.cancelLoading()
            loginEmailText.requestFocus()
            return
        }

        if (docPassword.isEmpty()) {
            loginPasswordText.error = "Please enter password"
            loading.cancelLoading()
            loginPasswordText.requestFocus()
            return
        }

        if (docPassword.length < 6) {
            loginPasswordText.error = "Minimum length of password should be 6"
            loading.cancelLoading()
            loginPasswordText.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(docEmail, docPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uID = user!!.uid
                    var db = FirebaseDatabase.getInstance().getReference("Doctor")
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var flag =
                                dataSnapshot.child(uID).child("email").getValue()
                            if(flag==null){
                                loading.cancelLoading()
                                Toast.makeText(
                                    baseContext, "Login failed, Please log in as a user.",
                                    Toast.LENGTH_LONG
                                ).show()
                                auth.signOut()
                            }else{
                                val storeData = storedData.edit()
                                storeData.putString("Role", "2")
                                storeData.commit()
                                loading.cancelLoading()
                                updateUI(user)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            loading.cancelLoading()
                            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                        }
                    }
                    db.addListenerForSingleValueEvent(postListener)


                } else {
                    loading.cancelLoading()
                    updateUI(null)
                }

            }

    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            startActivity(Intent(this, DoctorNavigationActivity::class.java))
            finish()
        } else {

            Toast.makeText(
                baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
