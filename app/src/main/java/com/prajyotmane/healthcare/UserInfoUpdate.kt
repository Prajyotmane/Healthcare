package com.prajyotmane.healthcare

import android.app.Activity
import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.userFirstName
import kotlinx.android.synthetic.main.activity_register.userLastName
import kotlinx.android.synthetic.main.activity_user_info_update.*
import kotlinx.android.synthetic.main.fragment_user_profile.userContact


class UserInfoUpdate : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var uID: String
    lateinit var loading: LoadingDialogBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_update)
        mAuth = FirebaseAuth.getInstance()
        uID = mAuth.currentUser!!.uid
        loading = LoadingDialogBox(this)

        setDefaults()

    }

    fun saveInformation(view: View) {
        loading.startLoading()
        var dbRef = FirebaseDatabase.getInstance().getReference("users")
        dbRef.child(uID).child("firstName").setValue(userFirstName.text.toString())
        dbRef.child(uID).child("lastName").setValue(userLastName.text.toString())
        dbRef.child(uID).child("contact").setValue(userContact.text.toString())
        dbRef.child(uID).child("Address").setValue(user_address.text.toString())
        dbRef.child(uID).child("City").setValue(user_city.text.toString())
        dbRef.child(uID).child("Zip").setValue(user_zip.text.toString())
        dbRef.child(uID).child("Gender").setValue(gender_select.selectedItemId)
        loading.cancelLoading()
        Toast.makeText(this, "Your information has been updated. Thank you!", Toast.LENGTH_SHORT)
        finish()

    }

    fun setDefaults() {
        loading.startLoading()
        var db = FirebaseDatabase.getInstance().getReference("users")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var fname =
                    dataSnapshot.child(uID).child("firstName").getValue()
                var lname =
                    dataSnapshot.child(uID).child("lastName").getValue()
                var contact =
                    dataSnapshot.child(uID).child("contact").getValue()
                var gendr =
                    dataSnapshot.child(uID).child("Gender").getValue()
                var adrs =
                    dataSnapshot.child(uID).child("Address").getValue()
                var savedCity =
                    dataSnapshot.child(uID).child("City").getValue()
                var savedZip =
                    dataSnapshot.child(uID).child("Zip").getValue()

                userFirstName.setText(fname.toString())
                userLastName.setText(lname.toString())
                userContact.setText(contact.toString())
                if (gendr == null) {
                    gender_select.setSelection(0)
                } else {
                    gender_select.setSelection(gendr.toString().toInt())
                }
                if (adrs != null) {
                    user_address.setText(adrs.toString())
                }
                if (savedCity != null) {
                    user_city.setText(savedCity.toString())
                }
                if (savedZip != null) {
                    user_zip.setText(savedZip.toString())
                }
                loading.cancelLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)
    }
}
