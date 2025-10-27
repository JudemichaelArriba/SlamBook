package com.kodego.diangca.ebrahim.myslambook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.diangca.ebrahim.myslambook.adapter.ListAdapters
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityListPageBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class listPage : AppCompatActivity() {

    private lateinit var binding: ActivityListPageBinding
    private val slamBooks = ArrayList<SlamBook>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent.getParcelableArrayListExtra<SlamBook>("slamBooks")?.let { list ->
            slamBooks.addAll(list)
        }

        binding.recycleview.layoutManager = LinearLayoutManager(this)
        binding.recycleview.adapter = ListAdapters(slamBooks)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
