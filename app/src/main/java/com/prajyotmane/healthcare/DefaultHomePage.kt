package com.prajyotmane.healthcare

import android.R.attr
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_default_home_page.view.curent_location
import java.util.*
import kotlin.collections.ArrayList


class DefaultHomePage : Fragment() {
    lateinit var mAuth: FirebaseAuth
    var AUTOCOMPLETE_REQUEST_CODE = 1
    lateinit var db: DatabaseReference
    lateinit var uID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        uID = mAuth.currentUser!!.uid
        db = FirebaseDatabase.getInstance().getReference("users")
        Places.initialize(activity!!.applicationContext, getString(R.string.api_key), Locale.US)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var currentCity =
                    dataSnapshot.child("users").child(uID).child("city").getValue()
                if (currentCity==null) {
                    curent_location.setText("Select your city")
                } else {
                    curent_location.setText(currentCity.toString())
                }
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
        view.button.setOnClickListener {
            mAuth?.signOut()
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

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
                Log.i(TAG, "Place: " + place.name + ", " + place.id)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(TAG, status.getStatusMessage())
            } else if (resultCode === AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
