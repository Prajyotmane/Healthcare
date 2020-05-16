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
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_doctor_appointment.*
import kotlinx.android.synthetic.main.fragment_doc_appointments.*
import kotlinx.android.synthetic.main.fragment_doc_appointments.doc_user_appointments
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorAppointment : AppCompatActivity() {

    var currentDate: Calendar? = Calendar.getInstance()
    lateinit var mAuth: FirebaseAuth
    private val currentUser = FirebaseAuth.getInstance().currentUser

    lateinit var db: DatabaseReference
    lateinit var ref: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var dateFormat = SimpleDateFormat("dd_MM_yyyy")
    lateinit var doctorId: String

    lateinit var loading: LoadingDialogBox

    var bookedUsersInfo: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    //lateinit var userList : ArrayList<ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointment)

        no_result.visibility = View.GONE

        db = FirebaseDatabase.getInstance().getReference("Doctor")

        mAuth = FirebaseAuth.getInstance()
        doctorId = mAuth.currentUser!!.uid
        loading = LoadingDialogBox(this)
        loading.startLoading()


        ref = FirebaseDatabase.getInstance().getReference("users")

        //bookedUsersInfo = ArrayList<ArrayList<String>>()

        var startDate = Calendar.getInstance()
        var endDate = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)
        endDate.add(Calendar.DATE, 4)

        var horizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.doc_calendar_view).range(startDate, endDate)
                .datesNumberOnScreen(3).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate).build()

        /*bookedUsersInfo.clear()
        userList.clear()*/

        bookedUsersInfo.clear()
        fetchUserData(dateFormat.format(startDate.time))
        //fetchUserInformation()
        //initRecycler()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {

            override fun onDateSelected(date: Calendar?, position: Int) {

                if (date?.timeInMillis != currentDate?.timeInMillis) {

                    currentDate = date

                    bookedUsersInfo.clear()
                    loading.startLoading()
                    fetchUserData(dateFormat.format(date?.time))
                    Log.d("Date", dateFormat.format(date?.time))

                }
            }
        }

    }

    private fun fetchUserData(appointDate: String?) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Date Key", appointDate.toString())

                var userList: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

                var userAptList =
                    dataSnapshot.child(doctorId).child("Appointments").child(appointDate.toString())
                //Log.d("XXXXXXXXXXXXXXXXX",doctorId.toString())

                for (bookedUser in userAptList.children) {

                    var temp: ArrayList<String> = ArrayList<String>()

                    var timeSlot = bookedUser.key.toString()
                    var bookedUserId = bookedUser.value.toString()

                    temp.add(timeSlot)
                    temp.add(bookedUserId)
                    temp.add(appointDate.toString())

                    userList.add(temp)
                }

/*                for(sid in userList){

                    Log.d("!!!!!!!!!!!!!!",sid[0].toString())
                    Log.d("!!!!!!!!!!!!!!",sid[1].toString())
                }*/

                fetchUserInformation(userList)
                Log.d("XXXXXXXXXXXXXXXX", userList.size.toString())
                //initRecycler(bookedUsersInfo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        db.addListenerForSingleValueEvent(postListener)

    }

    private fun fetchUserInformation(userList: ArrayList<ArrayList<String>>) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (person in userList) {

                    var userInfoArray: ArrayList<String> = arrayListOf()

                    var timing = person[0]
                    Log.d(person[0].toString(), person[1].toString())
                    Log.d("VVVVVVVVVVVV", "OOOOOOOOO")
                    var firstName =
                        dataSnapshot.child(person[1].toString()).child("firstName").getValue()
                    var lastName =
                        dataSnapshot.child(person[1].toString()).child("lastName").getValue()
                    var email = dataSnapshot.child(person[1].toString()).child("email").getValue()
                    var contact =
                        dataSnapshot.child(person[1].toString()).child("contact").getValue()
                    var zip = dataSnapshot.child(person[1].toString()).child("Zip").getValue()
                    var gender = dataSnapshot.child(person[1].toString()).child("Gender").getValue()
                    var city = dataSnapshot.child(person[1].toString()).child("City").getValue()
                    var address =
                        dataSnapshot.child(person[1].toString()).child("Address").getValue()

                    userInfoArray.add(firstName.toString())
                    userInfoArray.add(lastName.toString())
                    userInfoArray.add(email.toString())
                    userInfoArray.add(contact.toString())
                    userInfoArray.add(zip.toString())
                    userInfoArray.add(gender.toString())
                    userInfoArray.add(city.toString())
                    userInfoArray.add(address.toString())

                    userInfoArray.add(timing.toString())
                    userInfoArray.add(person[2])
                    userInfoArray.add(person[1])

                    Log.d("PPPPPPPPPPPPP", userInfoArray[8])
                    Log.d("PPPPPPPPPPPPP", userInfoArray[9])
                    bookedUsersInfo.add(userInfoArray)
                }

                initRecycler()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        ref.addListenerForSingleValueEvent(postListener)

        // return userList
    }


    private fun initRecycler() {

        Log.d("YYYYYYYYYYYYYYYYYYYYY", bookedUsersInfo.size.toString())

        if (bookedUsersInfo.size == 0) {
            no_result.visibility = View.VISIBLE
            doc_user_appointments.visibility = View.GONE
            loading.cancelLoading()
        } else {
            doc_user_appointments.visibility = View.VISIBLE
            viewManager = LinearLayoutManager(this)
            no_result.visibility = View.GONE
            viewAdapter = RecyclerMainAdapter(bookedUsersInfo, this, applicationContext)
            doc_user_appointments.layoutManager = viewManager
            doc_user_appointments.adapter = viewAdapter
            loading.cancelLoading()
        }


    }
}
