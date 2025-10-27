package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityMenuBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class MenuActivity : AppCompatActivity() {

    companion object {

        val slamBooks: ArrayList<SlamBook> = ArrayList()
    }

    private lateinit var binding: ActivityMenuBinding
    private var slamBook = SlamBook()

    private val launchRegister = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data

        Log.d("FROM REGISTER", data!!.getStringExtra("username").toString())
        Snackbar.make(
            binding.root,
            "Hi  ${data.getStringExtra("firstname")}! \n Please wait for the confirmation of your Account",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent.getParcelableExtra<SlamBook>("slamBooK")?.let { returnedBook ->
            slamBooks.add(returnedBook)
            Log.d("MenuActivity", "Received SlamBook: ${returnedBook.firstName}")
            Snackbar.make(binding.root, "SlamBook saved!", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnCreate.setOnClickListener {
            btnCreateOnClickListener()
        }

        binding.btnList.setOnClickListener {
            btnListClickListener()
        }
    }

    private fun btnListClickListener() {
        val nextForm = Intent(this, listPage::class.java)
        nextForm.putParcelableArrayListExtra("slamBooks", slamBooks)
        startActivity(nextForm)
    }

    private fun btnCreateOnClickListener() {
        val nextForm = Intent(this, FormActivity::class.java)
        nextForm.putExtra("slamBooK", slamBook)
        startActivity(nextForm)
    }
}
