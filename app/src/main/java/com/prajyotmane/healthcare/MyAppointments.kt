package com.prajyotmane.healthcare

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_doctor_list.*
import kotlinx.android.synthetic.main.activity_my_appointments.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class MyAppointments : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var loading: LoadingDialogBox
    lateinit var dList: ArrayList<ArrayList<String>>
    lateinit var tempList: ArrayList<ArrayList<String>>
    lateinit var uID: String
    lateinit var db: DatabaseReference
    lateinit var ddb: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)
        dList = arrayListOf()
        tempList = arrayListOf()
        loading = LoadingDialogBox(this)
        mAuth = FirebaseAuth.getInstance()
        uID = mAuth.currentUser!!.uid

        db = FirebaseDatabase.getInstance().getReference("users")
        ddb = FirebaseDatabase.getInstance().getReference("Doctor")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loading.startLoading()
                val myapts = dataSnapshot.child(uID).child("Appointments")

                for (snapshot in myapts.children) {
                    for (subsnapshot in snapshot.children) {
                        /*addDoctorToDataSet(
                            snapshot.key.toString(),
                            subsnapshot.key.toString(),
                            subsnapshot.getValue().toString()
                        )
                        */
                        tempList.add(arrayListOf(snapshot.key.toString(),
                            subsnapshot.key.toString(),
                            subsnapshot.getValue().toString()))
                    }
                }

                Log.d("Test",tempList.size.toString())
                addDoctorToDataSet()

              //  Log.d("Test ",dList.size.toString())

                //Populate RecyclerView with courses selected by the user


            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)

    }

    fun addDoctorToDataSet() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(each in tempList){
                    var dID = each[2]
                    var date = each[0]
                    var slot = each[1]
                    var name =
                        dataSnapshot.child(dID).child("firstName").getValue()?.toString()
                            .orEmpty() + " " + dataSnapshot.child(dID).child("lastName").getValue()
                            ?.toString().orEmpty()
                    var email =
                        dataSnapshot.child(dID).child("email").getValue().toString()
                    var contact =
                        dataSnapshot.child(dID).child("contact").getValue()?.toString().orEmpty()
                    var adrs =
                        dataSnapshot.child(dID).child("addressFirst").getValue()?.toString()
                            .orEmpty() + " " + dataSnapshot.child(dID).child("addressSecond").getValue()
                            ?.toString().orEmpty() + " " + dataSnapshot.child(dID).child("city")
                            .getValue()?.toString().orEmpty()
                    var spec =
                        dataSnapshot.child(dID).child("specialization").getValue().toString()

                    dList.add(arrayListOf(dID, name, email, contact, adrs, spec,date,slot))
                }

                if (dList.size == 0) {
                    no_results.text = "No results found"
                } else {
                    no_results.visibility = View.GONE
                    viewManager = LinearLayoutManager(applicationContext)
                    viewAdapter = MyAppointmentsAdapter(dList, uID, applicationContext,this@MyAppointments)
                    recyclerView = findViewById<RecyclerView>(R.id.my_appointments_recyclerview)
                    recyclerView.layoutManager = viewManager
                    recyclerView.adapter = viewAdapter
                }
                loading.cancelLoading()
                //Log.d("Test", dID+", "+name+", "+email+", "+contact+", "+adrs+", "+spec+", "+date+", "+slot)
                Log.d("Test",dList.size.toString())


            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        ddb.addListenerForSingleValueEvent(postListener)
    }
}
