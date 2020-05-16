package com.prajyotmane.healthcare

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.*;
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class DoctorListAdapter(
    private val dataSet: ArrayList<ArrayList<String>>,
    private var context: Context
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
        holder.doctordesc.text = dataSet[position][3]


        if (dataSet[position][4] != "null") {
            Picasso.get().load(dataSet[position][4]).into(holder.doctorPhoto)
        }


        holder.doctorListCard.setOnClickListener {
            var intent = Intent(context, DoctorDetailInfo::class.java)
            intent.putExtra("ID", dataSet[position][0])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val doctorPhoto: ImageView = view.findViewById(R.id.doctor_photo)
        val doctorName: TextView = view.findViewById(R.id.doctor_name)
        val doctorAddress: TextView = view.findViewById(R.id.doctor_address)
        val doctordesc: TextView = view.findViewById(R.id.doctor_desc)
        var doctorListCard: CardView = view.findViewById(R.id.doctor_list_card)
    }
}