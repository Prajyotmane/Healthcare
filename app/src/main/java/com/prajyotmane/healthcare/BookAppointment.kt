package com.prajyotmane.healthcare

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_book_appointment.*
import kotlinx.android.synthetic.main.activity_doctor_list.*
import kotlinx.android.synthetic.main.fragment_default_home_page.*
import java.text.SimpleDateFormat
import java.util.*

class BookAppointment : AppCompatActivity() {
    var currentDate: Calendar? = Calendar.getInstance()
    lateinit var mAuth: FirebaseAuth
    lateinit var db: DatabaseReference
    lateinit var dID: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var currentCity: String? = null
    var dataSet: MutableSet<String> = mutableSetOf<String>()
    lateinit var loading: LoadingDialogBox
    var dateFormat = SimpleDateFormat("dd_MM_yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        book_appointment.isEnabled = false

        dID = intent.getStringExtra("ID")
        loading = LoadingDialogBox(this)
        db = FirebaseDatabase.getInstance().getReference("Doctor")
        var startDate = Calendar.getInstance()
        var endDate = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)
        endDate.add(Calendar.DATE, 2)

        var horizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.calendar_view).range(startDate, endDate)
                .datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate).build()

        fetchAppointmentList(dateFormat.format(startDate.time))

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                if (date?.timeInMillis != currentDate?.timeInMillis) {
                    currentDate = date
                    book_appointment.isEnabled = false
                    dataSet = mutableSetOf<String>()
                    fetchAppointmentList(dateFormat.format(date?.time))
                    Log.d("Date", dateFormat.format(date?.time))

                    // Toast.makeText(applicationContext,dateFormat.format(date?.time),Toast.LENGTH_LONG).show()
                }
                //do something
            }
        }
    }

    private fun fetchAppointmentList(appointDate: String?) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Date Key", appointDate.toString())

                var aptList =
                    dataSnapshot.child(dID).child("Appointments").child(appointDate.toString())

                for (snapshot in aptList.children) {
                    dataSet.add(snapshot.key.toString())
                    Log.d("Key", snapshot.key.toString())
                }

                //Populate RecyclerView with courses selected by the user
                loading.cancelLoading()
                loadRecyclerView()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addListenerForSingleValueEvent(postListener)

    }

    fun loadRecyclerView() {
        viewManager = GridLayoutManager(this, 3)
        viewAdapter = SlotsAdapter(dataSet, this)

        recyclerView = slots_view
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

    fun bookAppointment(view: View){

    }


}
