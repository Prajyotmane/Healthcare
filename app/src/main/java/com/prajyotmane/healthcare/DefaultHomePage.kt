package com.prajyotmane.healthcare

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_default_home_page.*
import kotlinx.android.synthetic.main.fragment_default_home_page.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.util.*


class DefaultHomePage : Fragment() {
    lateinit var mAuth: FirebaseAuth
    var AUTOCOMPLETE_REQUEST_CODE = 1
    lateinit var db: DatabaseReference
    lateinit var uID: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var currentCity:String? = null
    lateinit var dataSet: Array<String>
    lateinit var loading: LoadingDialogBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        uID = mAuth.currentUser!!.uid
        dataSet = resources.getStringArray(R.array.category)
        loading = LoadingDialogBox(activity as Activity)
        db = FirebaseDatabase.getInstance().getReference("users")
        Places.initialize(activity!!.applicationContext, getString(R.string.api_key), Locale.US)
        loading.startLoading()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentCity =
                    dataSnapshot.child("users").child(uID).child("city").getValue().toString()
                if (currentCity==null) {
                    curent_location.setText("Select your city")
                } else {
                    curent_location.setText(currentCity.toString())
                }
                loadRecyclerView()
                loading.cancelLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_default_home_page, container, false)
        var fields = arrayListOf(Place.Field.ID, Place.Field.NAME)



        view.location_layout.setOnClickListener {

            var intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).setTypeFilter(TypeFilter.CITIES)
                .setCountries(mutableListOf("US"))
                .build(activity!!.applicationContext)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode === AutocompleteActivity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                db.child("users").child(uID).child("city").setValue(place.name)
                curent_location.setText(place.name)
                currentCity = place.name
                Log.i(TAG, "Place: " + place.name + ", " + place.id)
                loadRecyclerView()
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(TAG, status.getStatusMessage())
            } else if (resultCode === AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }
    fun loadRecyclerView()
    {
        viewManager = GridLayoutManager(context,2)
        viewAdapter = DoctorCategoryListAdapter(dataSet,context!!,currentCity.toString())

        recyclerView = course_list
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

}
