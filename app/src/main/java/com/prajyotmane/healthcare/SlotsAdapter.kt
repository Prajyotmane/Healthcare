package com.prajyotmane.healthcare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color.GREEN
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class SlotsAdapter(
    private val dataSet: MutableSet<String>,
    private var slotData: SlotDataClass,
    private var context: Context
) :
    RecyclerView.Adapter<SlotsAdapter.MyViewHolder>() {
    lateinit var parent: ViewGroup
    var slotList = context.resources.getStringArray(R.array.slots)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlotsAdapter.MyViewHolder {

        //Setting up the inflater with custom view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_slot_layout, parent, false)

        //Setting up ViewGroup to global variable. I have used this for context in Toast
        this.parent = parent

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.slotTitle.text = slotList[position]
        if (position.toString() in dataSet) {
            holder.slotStatue.text = "Unavailable"
            holder.slotCard.setCardBackgroundColor(context.getColor(R.color.red))

        } else {
            holder.slotStatue.text = "Available"
            holder.slotCard.setCardBackgroundColor(context.getColor(R.color.green))
        }

        holder.slotCard.setOnClickListener {
            if (holder.slotStatue.text == "Unavailable") {
                Toast.makeText(context, "The slot is unavailable", Toast.LENGTH_LONG).show()

            } else {
                if (slotData.holder != null) {
                    slotData.holder?.slotCard?.setBackgroundColor(context.getColor(R.color.green))
                }
                slotData.slotID = position.toString()
                slotData.holder = holder
                slotData.slot = slotList[position]
                (context as Activity).findViewById<Button>(R.id.book_appointment).isEnabled = true
                holder.slotCard.setBackgroundColor(context.getColor(R.color.selected))

            }
        }

    }

    override fun getItemCount() = slotList.size

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val slotTitle: TextView = view.findViewById(R.id.slot_title)
        val slotStatue: TextView = view.findViewById(R.id.slot_status)
        var slotCard: CardView = view.findViewById(R.id.slot_card)
    }
}
