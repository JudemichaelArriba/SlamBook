package com.kodego.diangca.ebrahim.myslambook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kodego.diangca.ebrahim.myslambook.R
import com.kodego.diangca.ebrahim.myslambook.model.Movie

class AdapterMovie2(
    var context: Context, var movies: ArrayList<Movie>
) : RecyclerView.Adapter<AdapterMovie2.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_single, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.itemName.text = movie.movieName
    }

    override fun getItemCount(): Int = movies.size
}
