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
import com.squareup.picasso.Picasso
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

                var address1 =
                    dataSnapshot.child(doctorID).child("addressFirst").getValue()
                var address2 =
                    dataSnapshot.child(doctorID).child("addressSecond").getValue()
                var zip =
                    dataSnapshot.child(doctorID).child("pincode").getValue()
                var city =
                    dataSnapshot.child(doctorID).child("city").getValue()
                var exper =
                    dataSnapshot.child(doctorID).child("experience").getValue()
                var spec =
                    dataSnapshot.child(doctorID).child("specialization").getValue()
                var desc =
                    dataSnapshot.child(doctorID).child("description").getValue()
                var degree =
                    dataSnapshot.child(doctorID).child("degree").getValue()
                var url = dataSnapshot.child(doctorID).child("PhotoURL").getValue().toString()

                dinfo_name.setText("Name: "+fname.toString() + " " + lname.toString())
                dinfo_contact.setText("Email: "+email.toString())
                dinfo_address.setText("Address: "+address1.toString() + " " + address2?.toString()+" "+city?.toString()+"-"+zip.toString())

                dinfo_email.text = "Email: "+email.toString()

                if(contact!=null){
                    dinfo_contact.setText("Contact: "+contact?.toString())
                }else{
                    dinfo_contact.visibility = View.GONE
                }
                var tmp = ""
                if(degree!=null){
                    tmp=degree.toString()+" ("+spec+")"
                }
                else{
                    tmp = spec.toString()
                }
                dinfo_desc.text = tmp
                if(desc!= null){
                    dinfo_detail.text = "Description: "+desc.toString()
                }else{
                    dinfo_detail.visibility = View.GONE
                }

                if(exper!=null){
                    dinfo_yoe.text = "Year of Experience: "+exper.toString()
                }else{
                    dinfo_yoe.visibility = View.GONE
                }
                if(url!="null"){
                    Picasso.get().load(url).into(profile_pic)
                }

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
