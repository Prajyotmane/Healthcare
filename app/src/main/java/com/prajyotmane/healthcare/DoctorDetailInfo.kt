package com.prajyotmane.healthcare

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_doctor_detail_info.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class DoctorDetailInfo : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var loading: LoadingDialogBox
    lateinit var uID: String
    lateinit var doctorID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail_info)
        mAuth = FirebaseAuth.getInstance()
        loading = LoadingDialogBox(this)
        uID = mAuth.currentUser!!.uid
        doctorID = intent.getStringExtra("ID")
        var storageRef = FirebaseStorage.getInstance().getReference("pics").child(doctorID)
        Glide.with(applicationContext).load(storageRef)
            .into(profile_pic)
        //Log.d("Image", temp.bucket + " " + temp.path)
        var db = FirebaseDatabase.getInstance().getReference("Doctor")
        loading.startLoading()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var fname =
                    dataSnapshot.child(doctorID).child("firstName").getValue()
                var lname =
                    dataSnapshot.child(doctorID).child("lastName").getValue()
                var email =
                    dataSnapshot.child(doctorID).child("email").getValue()
                var contact =
                    dataSnapshot.child(doctorID).child("contact").getValue()
                var gendr =
                    dataSnapshot.child(doctorID).child("Gender").getValue()
                var address1 =
                    dataSnapshot.child(doctorID).child("addressFirst").getValue()
                var address2 =
                    dataSnapshot.child(doctorID).child("addressSecond").getValue()
                var zip =
                    dataSnapshot.child(doctorID).child("pincode").getValue()
                var city =
                    dataSnapshot.child(doctorID).child("city").getValue()

                dinfo_name.setText(fname.toString() + " " + lname.toString())
                dinfo_contact.setText(email.toString())
                dinfo_address.setText(address1.toString() + " " + address2.toString())

                loading.cancelLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addValueEventListener(postListener)
    }

    fun bookAppointment(view: View) {
        var intent = Intent(this, BookAppointment::class.java)
        intent.putExtra("ID", doctorID)
        startActivity(intent)
        //Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
    }
}
