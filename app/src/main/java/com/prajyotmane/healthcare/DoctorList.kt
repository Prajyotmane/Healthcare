package com.prajyotmane.healthcare

import android.content.ContentValues
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_doctor_list.*


class DoctorList : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var loading: LoadingDialogBox
    var doctorList: ArrayList<ArrayList<String>> = arrayListOf()
    lateinit var userCity: String
    lateinit var userCategory: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        mAuth = FirebaseAuth.getInstance()
        userCity = intent.getStringExtra("city")
        userCategory = intent.getStringExtra("category")
        Log.d("City", userCity)
        loading = LoadingDialogBox(this@DoctorList)
        loading.startLoading()
        var db = FirebaseDatabase.getInstance().getReference("Doctor")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    if (snapshot.child("city").getValue()
                            .toString() == userCity && snapshot.child("specialization").getValue()
                            .toString() == userCategory
                    ) {
                        val dID = snapshot.key.toString()
                        val name = snapshot.child("firstName").getValue()
                            .toString() + " " + snapshot.child("lastName").getValue().toString()
                        val address = snapshot.child("addressFirst").getValue()
                            .toString() + " " + snapshot.child("addressSecond").getValue()
                            .toString()
                        val contact = snapshot.child("contact").getValue().toString()
                        doctorList.add(arrayListOf(dID, name, address, contact))
                        Log.d("Doctor", name + " " + dID)
                    }
                }

                if(doctorList.size==0){
                    no_result_found.text = "No results found"
                }
                else{
                    no_result_found.visibility = View.GONE
                    viewManager = LinearLayoutManager(applicationContext)
                    viewAdapter = DoctorListAdapter(doctorList)
                    recyclerView = findViewById<RecyclerView>(R.id.doctorListRecyclerView)
                    recyclerView.layoutManager = viewManager
                    recyclerView.adapter = viewAdapter
                }
                //Populate RecyclerView with courses selected by the user
                loading.cancelLoading()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)

    }

    /*var handler = Handler()
    handler.postDelayed(Runnable {
        if(iscomplete){
            loading.cancelLoading()
        }
        else{
            handler.postDelayed(this, 500)
        }
    },500)*/

}
