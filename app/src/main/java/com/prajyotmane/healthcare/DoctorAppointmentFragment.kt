package com.prajyotmane.healthcare

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_doc_appointments.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorAppointmentFragment : Fragment() {

    var currentDate: Calendar? = Calendar.getInstance()
    lateinit var mAuth: FirebaseAuth
    private val currentUser = FirebaseAuth.getInstance().currentUser

    lateinit var db: DatabaseReference
    lateinit var ref: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var layoutManager: LinearLayoutManager


    var dateFormat = SimpleDateFormat("dd_MM_yyyy")
    lateinit var doctorId: String
    lateinit var bookedUsersInfo : ArrayList<ArrayList<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance().getReference("Doctor")
        mAuth = FirebaseAuth.getInstance()
        doctorId = mAuth.currentUser!!.uid

        var startDate = Calendar.getInstance()
        var endDate = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)
        endDate.add(Calendar.DATE, 2)

        var horizontalCalendar =
            HorizontalCalendar.Builder(view, R.id.doc_calendar_view).range(startDate, endDate)
                .datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate).build()

        fetchUserData(dateFormat.format(startDate.time))

        horizontalCalendar.calendarListener = object :HorizontalCalendarListener(){

            override fun onDateSelected(date: Calendar?, position: Int) {

                if (date?.timeInMillis != currentDate?.timeInMillis) {
                    currentDate = date
                    fetchUserData(dateFormat.format(date?.time))
                    Log.d("Date", dateFormat.format(date?.time))

                }
            }
        }

    }

    private fun fetchUserData(appointDate: String?){

        val postListener = object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Date Key", appointDate.toString())

                var userAptList = dataSnapshot.child(doctorId).child("Appointments").child(appointDate.toString())

                    for (bookedUser in userAptList.children){
                        var timeSlot = bookedUser.key.toString()
                        var bookedUserId = bookedUser.value.toString()

                        var userList : ArrayList<String> = fetchUserInformation(bookedUserId)
                        userList.add(timeSlot)
                        bookedUsersInfo.add(userList)
                        userList.clear()
                    }

                initRecycler(bookedUsersInfo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        db.addListenerForSingleValueEvent(postListener)

    }

    private fun fetchUserInformation(bookedUserId : String):ArrayList<String>{

        var userList:ArrayList<String> = ArrayList<String>()
        ref = FirebaseDatabase.getInstance().getReference("User")

        val postListener = object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var firstName = dataSnapshot.child(bookedUserId).child("firstName").getValue()
                var lastName = dataSnapshot.child(bookedUserId).child("lastName").getValue()
                var email = dataSnapshot.child(bookedUserId).child("email").getValue()
                var contact = dataSnapshot.child(bookedUserId).child("contact").getValue()
                var zip = dataSnapshot.child(bookedUserId).child("Zip").getValue()
                var gender = dataSnapshot.child(bookedUserId).child("Gender").getValue()
                var city = dataSnapshot.child(bookedUserId).child("City").getValue()
                var address = dataSnapshot.child(bookedUserId).child("Address").getValue()

                userList.add(firstName.toString())
                userList.add(lastName.toString())
                userList.add(email.toString())
                userList.add(contact.toString())
                userList.add(zip.toString())
                userList.add(gender.toString())
                userList.add(city.toString())
                userList.add(address.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        ref.addListenerForSingleValueEvent(postListener)
        return userList

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doc_appointments,container,false)
    }

    private fun initRecycler(bookedUsersInfo: ArrayList<ArrayList<String>>){

    /*    layoutManager= LinearLayoutManager(context)

        viewAdapter = RecyclerMainAdapter(bookedUsersInfo)
        doc_user_appointments.layoutManager =layoutManager
        doc_user_appointments.adapter = this.viewAdapter

        bookedUsersInfo.clear()
*/
    }

}