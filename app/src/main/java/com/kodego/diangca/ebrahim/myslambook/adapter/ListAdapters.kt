package com.kodego.diangca.ebrahim.myslambook.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kodego.diangca.ebrahim.myslambook.R
import com.kodego.diangca.ebrahim.myslambook.databinding.ItemSlambooksBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import com.kodego.diangca.ebrahim.myslambook.slamBookInfo

class ListAdapters(private val slamBooks: List<SlamBook>) :
    RecyclerView.Adapter<ListAdapters.SlamBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlamBookViewHolder {
        val binding = ItemSlambooksBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SlamBookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlamBookViewHolder, position: Int) {
        val slamBook = slamBooks[position]
        holder.bind(slamBook)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, slamBookInfo::class.java)
            intent.putExtra("slamBookData", slamBook)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = slamBooks.size

    inner class SlamBookViewHolder(private val binding: ItemSlambooksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(slamBook: SlamBook) {
            binding.fullname.text = "${slamBook.firstName} ${slamBook.lastName}"
            binding.birthdate.text = slamBook.birthDate ?: "No birthdate"
            binding.contactNo.text = slamBook.contactNo ?: "No contact"
            binding.addressTv.text = slamBook.address ?: "No address"


            val profileUri = slamBook.profilePic
                ?.let { Uri.parse(it) }

            Glide.with(binding.root.context)
                .load(profileUri ?: R.drawable.sc_profile)
                .placeholder(R.drawable.sc_profile)
                .error(R.drawable.sc_profile)
                .into(binding.profileImage)
        }
    }
}
