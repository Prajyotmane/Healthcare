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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DoctorListAdapter(
    private val dataSet: ArrayList<ArrayList<String>>,
    private var context: Context
) :
    RecyclerView.Adapter<DoctorListAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup
    lateinit var storageRef: StorageReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorListAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_list_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent
        storageRef = FirebaseStorage.getInstance().getReference("pics")
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Populating view in holder for recyclerView
        holder.doctorName.text = dataSet[position][1]
        holder.doctorAddress.text = dataSet[position][2]
        holder.doctorContact.text = dataSet[position][3]

        val temp = storageRef.child(dataSet[position][0])
        Glide.with(context).load(temp)
            .into(holder.doctorPhoto)

        Log.d("Image",temp.bucket+" "+temp.path)

        holder.doctorListCard.setOnClickListener {
            var intent = Intent(context,DoctorDetailInfo::class.java)
            intent.putExtra("ID",dataSet[position][0])
            context.startActivity(intent)
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