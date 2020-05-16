package com.prajyotmane.healthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_doctor_registration.*
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import jdk.nashorn.internal.runtime.ECMAException.getException
import com.google.firebase.auth.FirebaseUser
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class DoctorRegistration : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var storedData: SharedPreferences
    private lateinit var loading: LoadingDialogBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_registration)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        loading = LoadingDialogBox(this)


        ArrayAdapter.createFromResource(
            this,
            R.array.doc_spl_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSpecialization.adapter = adapter
        }

        docRegButton.setOnClickListener {
            loading.startLoading()
            registerDoctor()
        }
    }

    private fun registerDoctor() {
        register_progress.setVisibility(View.VISIBLE)
        val firstName = fnameText.text.toString()
        val lastName = lnameText.text.toString()
        val docEmail = emailText.text.toString()
        val docPassword = passwordText.text.toString()
        val firstAddress = addr1Text.text.toString()
        val secndLineAddress = addr2Text.text.toString()
        val docCity = cityText.text.toString()
        val docState = stateText.text.toString()
        val docPin = pinText.text.toString()
        val docSpecialization = spinnerSpecialization.selectedItem.toString()

        if (firstName.isEmpty()) {
            fnameText.error = "Please enter first name"
            fnameText.requestFocus()
            loading.cancelLoading()
            return
        }

        if (lastName.isEmpty()) {
            lnameText.error = "Please enter last name"
            loading.cancelLoading()
            lnameText.requestFocus()
            return
        }

        if (docEmail.isEmpty()) {
            emailText.error = "Please enter email"
            loading.cancelLoading()
            emailText.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(docEmail).matches()) {
            emailText.error = "Please enter a valid email"
            loading.cancelLoading()
            emailText.requestFocus()
            return
        }

        if (docPassword.isEmpty()) {
            passwordText.error = "Please enter password"
            loading.cancelLoading()
            passwordText.requestFocus()
            return
        }

        if (docPassword.length < 6) {
            passwordText.error = "Minimum length of password should be 6"
            loading.cancelLoading()
            passwordText.requestFocus()
            return
        }

        if (firstAddress.isEmpty()) {
            addr1Text.error = "Please enter Address"
            loading.cancelLoading()
            addr1Text.requestFocus()
            return
        }

        if (secndLineAddress.isEmpty()) {
            addr2Text.error = "Please enter Address"
            loading.cancelLoading()
            addr2Text.requestFocus()
            return
        }

        if (docCity.isEmpty()) {
            cityText.error = "Please enter city"
            loading.cancelLoading()
            cityText.requestFocus()
            return
        }

        if (docState.isEmpty()) {
            stateText.error = "Please enter State"
            loading.cancelLoading()
            stateText.requestFocus()
            return
        }

        if (docPin.isEmpty()) {
            pinText.error = "Please enter pincode"
            loading.cancelLoading()
            pinText.requestFocus()
            return
        }

        mAuth.createUserWithEmailAndPassword(docEmail, docPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = mAuth.getCurrentUser()

                    val ref = FirebaseDatabase.getInstance().getReference("Doctor")

                    //val doctorId = ref.push().key.toString()
                    val doctorId = mAuth.currentUser!!.uid
                    val doctor = Doctor(
                        doctorId,
                        firstName,
                        lastName,
                        docEmail,
                        docPassword,
                        firstAddress,
                        secndLineAddress,
                        docCity,
                        docState,
                        docPin,
                        docSpecialization
                    )

                    loading.cancelLoading()
                    ref.child(doctorId).setValue(doctor).addOnCompleteListener {

                        Toast.makeText(
                            applicationContext,
                            "Doctor Registration Successful",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this, DoctorLogin::class.java))
                        finish()
                    }

                    //updateUI(user)

                } else {
                    loading.cancelLoading()
                    Toast.makeText(
                        baseContext, "Account already exits.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


    }

}
