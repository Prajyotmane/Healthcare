package com.prajyotmane.healthcare

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_confirm_slot.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.userContact
import kotlinx.android.synthetic.main.activity_register.userFirstName
import kotlinx.android.synthetic.main.activity_register.userLastName
import kotlinx.android.synthetic.main.activity_user_info_update.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class ConfirmSlot : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var db: DatabaseReference
    lateinit var dID: String
    lateinit var loading: LoadingDialogBox
    lateinit var slot: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_slot)
        loading = LoadingDialogBox(this)
        loading.startLoading()
        dID = intent.getStringExtra("ID")
        slot = intent.getStringExtra("Slot")
        booking_info.setText("Booking confirmed from " + slot)
        db = FirebaseDatabase.getInstance().getReference("Doctor")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var fname =
                    dataSnapshot.child(dID).child("firstName").getValue()
                var lname =
                    dataSnapshot.child(dID).child("lastName").getValue()
                var contact =
                    dataSnapshot.child(dID).child("contact").getValue()
                var email =
                    dataSnapshot.child(dID).child("email").getValue()
                var adrs =
                    dataSnapshot.child(dID).child("Address").getValue()
                var savedCity =
                    dataSnapshot.child(dID).child("City").getValue()
                var savedZip =
                    dataSnapshot.child(dID).child("Zip").getValue()

                dconf_Name.setText(fname.toString() + " " + lname.toString())
                dconf_Address.setText(adrs.toString() + ", " + savedCity.toString() + " - " + savedZip.toString())
                dconf_Contact.setText(contact.toString())
                dconf_Email.setText(email.toString())
                loading.cancelLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)
    }

    fun doneBooking(view: View) {
        var intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        this.finish()
    }
}
