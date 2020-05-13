package com.prajyotmane.healthcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DoctorListAdapter(
    private val dataSet: ArrayList<ArrayList<String>>
) :
    RecyclerView.Adapter<DoctorListAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorListAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_list_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Populating view in holder for recyclerView
        holder.doctorName.text = dataSet[position][1]
        holder.doctorAddress.text = dataSet[position][2]
        holder.doctorContact.text = dataSet[position][3]

        holder.doctorListCard.setOnClickListener {

        }
    }

    override fun getItemCount() = dataSet.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val doctorPhoto: ImageView = view.findViewById(R.id.doctor_photo)
        val doctorName: TextView = view.findViewById(R.id.doctor_name)
        val doctorAddress: TextView = view.findViewById(R.id.doctor_address)
        val doctorContact: TextView = view.findViewById(R.id.doctor_contact)
        var doctorListCard: CardView = view.findViewById(R.id.doctor_list_card)
    }
}