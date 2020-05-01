package com.prajyotmane.healthcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DoctorListAdapter(
    private val dataSet: Array<String>
) :
    RecyclerView.Adapter<DoctorListAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorListAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_category_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Populating view in holder for recyclerView
        holder.doctorCategoty.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val doctorLogo: ImageView = view.findViewById(R.id.doctor_category_logo)
        val doctorCategoty: TextView = view.findViewById(R.id.doctor_category_title)
    }
}