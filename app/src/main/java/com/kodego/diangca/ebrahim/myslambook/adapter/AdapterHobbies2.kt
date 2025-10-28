package com.kodego.diangca.ebrahim.myslambook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kodego.diangca.ebrahim.myslambook.R
import com.kodego.diangca.ebrahim.myslambook.model.Hobbies

class AdapterHobbies2(
    var context: Context, var hobbies: ArrayList<Hobbies>
) : RecyclerView.Adapter<AdapterHobbies2.HobbiesViewHolder>() {

    inner class HobbiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbiesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_single, parent, false)
        return HobbiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HobbiesViewHolder, position: Int) {
        val hobby = hobbies[position]
        holder.itemName.text = hobby.hobbie
    }

    override fun getItemCount(): Int = hobbies.size
}
