package com.prajyotmane.healthcare

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_default_home_page.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

class UserProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mAuth: FirebaseAuth
    lateinit var loading: LoadingDialogBox
    lateinit var postListener:ValueEventListener
    lateinit var db:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        loading = LoadingDialogBox(activity as Activity)
        var uID = mAuth.currentUser!!.uid
        db = FirebaseDatabase.getInstance().getReference("users")
        loading.startLoading()
        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var fname =
                    dataSnapshot.child(uID).child("firstName").getValue()
                var lname =
                    dataSnapshot.child(uID).child("lastName").getValue()
                var email =
                    dataSnapshot.child(uID).child("email").getValue()
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

                userName.setText(fname.toString() + " " + lname.toString())
                userEmail.setText(email.toString())
                userContact.setText(contact.toString())
                if(gendr==null){
                    gender.text = "Not specified"
                }
                else{
                    if(gendr.toString() == "0"){
                        gender.text = "Not Specified"
                    }else if(gendr.toString()=="1"){
                        gender.text = "Male"
                    }else if(gendr.toString()=="2"){
                        gender.text = "Female"
                    }else{
                        gender.text = "Other"
                    }

                }
                if(adrs==null || adrs.toString().length==0){
                    address.text = "Not specified"
                }
                else{
                    address.text = adrs.toString()
                }
                if(savedCity==null || savedCity.toString().length==0){
                    city.text = "Not specified"
                }
                else{
                    city.text = savedCity.toString()
                }
                if(savedZip==null || savedZip.toString().length==0){
                    zip.text = "Not specified"
                }
                else{
                    zip.text = savedZip.toString()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        view.editInformation.setOnClickListener {
            startActivity(Intent(context,UserInfoUpdate::class.java))
        }
        return view
    }

    override fun onDestroy() {
        db.removeEventListener(postListener)
        super.onDestroy()
    }

}
