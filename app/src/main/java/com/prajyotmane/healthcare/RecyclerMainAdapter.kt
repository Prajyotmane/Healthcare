package com.prajyotmane.healthcare

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.doc_user_info.view.*

class RecyclerMainAdapter(
    private val bookedUserInfo: ArrayList<ArrayList<String>>,
    private var activity: DoctorAppointment,
    private var context: Context
) :
    RecyclerView.Adapter<RecyclerMainAdapter.DisplayBookedUserViewHolder>() {
    var slotArray = context.resources.getStringArray(R.array.slots)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayBookedUserViewHolder {

        return DisplayBookedUserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.doc_user_info, parent, false)
        )

    }

    //It returns the total number of times we want card view to display
    override fun getItemCount(): Int {

        return bookedUserInfo.size
    }

    override fun onBindViewHolder(holder: DisplayBookedUserViewHolder, position: Int) {

        //Fetches information from arrayList and set it on text field
        var ddmmyy = bookedUserInfo[position][9].split("_")
        holder.fName.setText(bookedUserInfo[position][0] + " " + bookedUserInfo[position][1])
        // holder.lName.setText(bookedUserInfo[position][1])
        holder.email.setText(bookedUserInfo[position][2])
        holder.contact.setText(bookedUserInfo[position][3])
        //holder.zip.setText(bookedUserInfo[position][4])
        //holder.gender.setText(bookedUserInfo[position][5])
        //holder.city.setText(bookedUserInfo[position][6])
        var temp = ""
        if(bookedUserInfo[position][7]!="null"){
            temp+=bookedUserInfo[position][7]+" "
        }
        if(bookedUserInfo[position][6]!="null"){
            temp+=bookedUserInfo[position][6]+" "
        }
        if(bookedUserInfo[position][4]!="null"){
            temp+=bookedUserInfo[position][4]
        }
        holder.address.setText(temp)
        holder.timeSlot.setText("Time: " + slotArray[bookedUserInfo[position][8].toInt()])
        holder.appointDate.setText("Date: " + ddmmyy[1] + "/" + ddmmyy[0] + "/" + ddmmyy[2])

        //Log.d("RRr",bookedUserInfo[position][8])

/*        holder.cancel.setOnClickListener {

            var auth: FirebaseAuth
            auth = FirebaseAuth.getInstance()
            var doctorId = auth.currentUser!!.uid

            var db = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorId).child("Appointments")
                .child()

        }*/

        holder.cancel.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(activity)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you really want to cancel the appointment?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    var loading = LoadingDialogBox(activity)
                    loading.startLoading()
                    var auth: FirebaseAuth
                    auth = FirebaseAuth.getInstance()
                    var doctorId = auth.currentUser!!.uid

                    var db = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorId)
                        .child("Appointments")
                        .child(bookedUserInfo[position][9]).child(bookedUserInfo[position][8])
                    db.removeValue()


                    db = FirebaseDatabase.getInstance().getReference("users")
                        .child(bookedUserInfo[position][10])
                        .child("Appointments").child(bookedUserInfo[position][9])
                        .child(bookedUserInfo[position][8])
                    db.removeValue()

                    context.startActivity(Intent(context, DoctorAppointment::class.java))
                    (activity).finish()

                    loading.cancelLoading()
                })
                // negative button text and action
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Cancel Appointment")
            // show alert dialog
            alert.show()


        }


    }


    class DisplayBookedUserViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        //Associating views inside doc_user_info.xml

        val fName = itemView.doc_userfname

        //val lName = itemView.doc_userlname
        val email = itemView.doc_useremail
        val contact = itemView.doc_usercontact

        //val zip = itemView.doc_userzip
        //val gender = itemView.doc_usergender
        //val city = itemView.doc_usercity
        val address = itemView.doc_useraddress
        val timeSlot = itemView.doc_appointmentslot
        val appointDate = itemView.list_date

        val cancel = itemView.deleteAppointment

    }


}