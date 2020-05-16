package com.prajyotmane.healthcare

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class DoctorCategoryListAdapter(
    private val dataSet: Array<String>,
    private var context: Context,
    private var city: String
) :
    RecyclerView.Adapter<DoctorCategoryListAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorCategoryListAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_category_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Populating view in holder for recyclerView
        var title = dataSet[position].split(" ").joinToString("_").toLowerCase()
        var id =
            context.resources.getIdentifier(title, "drawable", context.packageName)


        holder.doctorCategoty.text = dataSet[position]
        holder.doctorLogo.setImageResource(id)
        //Log.d("Image", id.toString())
        holder.categotyCard.setOnClickListener {
            Log.d("Selected", holder.doctorCategoty.text.toString())
            if (city!="null") {
                var intent = Intent(context, DoctorList::class.java)
                intent.putExtra("city", city)
                intent.putExtra("category", dataSet[position])
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Please select your city first", Toast.LENGTH_LONG).show()
            }


        }
    }

    override fun getItemCount() = dataSet.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val doctorLogo: ImageView = view.findViewById(R.id.doctor_category_logo)
        val doctorCategoty: TextView = view.findViewById(R.id.doctor_category_title)
        var categotyCard: CardView = view.findViewById(R.id.category_card)
    }
}