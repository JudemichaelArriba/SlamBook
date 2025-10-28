package com.kodego.diangca.ebrahim.myslambook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kodego.diangca.ebrahim.myslambook.R
import com.kodego.diangca.ebrahim.myslambook.model.Skill

class AdapterSkills2(
    var context: Context,
    var skills: ArrayList<Skill>
) : RecyclerView.Adapter<AdapterSkills2.SkillViewHolder>() {

    inner class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val rateTv: TextView = itemView.findViewById(R.id.rateTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_withrate, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill = skills[position]
        holder.itemName.text = skill.skill
        holder.rateTv.text = "Rate: ${skill.rate}"
    }

    override fun getItemCount(): Int = skills.size
}
