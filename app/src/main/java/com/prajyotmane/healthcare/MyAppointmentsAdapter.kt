package com.prajyotmane.healthcare

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase


class MyAppointmentsAdapter(
    private val dataSet: ArrayList<ArrayList<String>>,
    private val uID: String,
    private var context: Context,
    private var activity: Activity
) :
    RecyclerView.Adapter<MyAppointmentsAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup
    var slotArray = context.resources.getStringArray(R.array.slots)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAppointmentsAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_appointments_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Populating view in holder for recyclerView
        holder.doctorName.text = dataSet[position][1] + " " + dataSet[position][5]
        holder.doctorAddress.text = dataSet[position][4]
        holder.doctorContact.text = dataSet[position][2]
        holder.date.text = dataSet[position][6]
        holder.slot.text = slotArray[dataSet[position][7].toInt()]

        holder.cancel.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(activity)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you really want to cancel the appointment?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes", DialogInterface.OnClickListener {
                        dialog, id -> var loading = LoadingDialogBox(activity)
                    loading.startLoading()

                    var db = FirebaseDatabase.getInstance().getReference("users").child(uID)
                        .child("Appointments").child(dataSet[position][6])
                        .child(dataSet[position][7])
                    db.removeValue()

                    db = FirebaseDatabase.getInstance().getReference("Doctor")
                        .child(dataSet[position][0])
                        .child("Appointments").child(dataSet[position][6])
                        .child(dataSet[position][7])
                    db.removeValue()
                    context.startActivity(Intent(context, MyAppointments::class.java))
                    (activity).finish()
                    loading.cancelLoading()
                })
                // negative button text and action
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Cancel Appointment")
            // show alert dialog
            alert.show()




        }
    }

    override fun getItemCount() = dataSet.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val doctorPhoto: ImageView = view.findViewById(R.id.list_doctor_photo)
        val doctorName: TextView = view.findViewById(R.id.list_doctor_name)
        val doctorAddress: TextView = view.findViewById(R.id.list_doctor_address)
        val doctorContact: TextView = view.findViewById(R.id.list_doctor_contact)
        val date: TextView = view.findViewById(R.id.list_date)
        val slot: TextView = view.findViewById(R.id.list_slot)
        var cancel: Button = view.findViewById(R.id.list_cancel)
    }
}