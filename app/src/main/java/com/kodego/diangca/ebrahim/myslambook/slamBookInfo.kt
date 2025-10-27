package com.kodego.diangca.ebrahim.myslambook

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSong
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSong2
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivitySlamBookInfoBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class slamBookInfo : AppCompatActivity() {

    private lateinit var binding: ActivitySlamBookInfoBinding
    private lateinit var adapterSong2: AdapterSong2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlamBookInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slamBook = intent.getParcelableExtra<SlamBook>("slamBookData")

        slamBook?.let {

            binding.fullNameTv.text = "${it.firstName} ${it.lastName}"
            binding.nickNameTv.text = it.nickName ?: "N/A"
            binding.friendCallMeTv.text = it.friendCallMe ?: "N/A"
            binding.likeToCallMeTv.text = it.likeToCallMe ?: "N/A"
            binding.birthDateTv.text = it.birthDate ?: "N/A"
            binding.genderTv.text = it.gender ?: "N/A"
            binding.statusTv.text = it.status ?: "N/A"
            binding.emailTv.text = it.email ?: "N/A"
            binding.contactNoTv.text = it.contactNo ?: "N/A"
            binding.addressTv.text = it.address ?: "N/A"



            Log.d("favo", it.favoriteSongs.toString())


            val favoriteSongs = it.favoriteSongs ?: arrayListOf()

            adapterSong2 = AdapterSong2(this, favoriteSongs)
            binding.favSongList.layoutManager = LinearLayoutManager(this)
            binding.favSongList.adapter = adapterSong2




            binding.defineLoveTv.text = it.defineLove ?: ""
            binding.defineFriendshipTv.text = it.defineFriendship ?: ""
            binding.memorableExperienceTV.text = it.memorableExperience ?: ""
            binding.describeMeTv.text = it.describeMe ?: ""
            binding.adviceForMeTv.text = it.adviceForMe ?: ""


            val profileUri = it.profilePic?.let { Uri.parse(it) }
            Glide.with(this)
                .load(profileUri ?: R.drawable.sc_profile)
                .placeholder(R.drawable.sc_profile)
                .error(R.drawable.sc_profile)
                .into(binding.profileImageView)
        }


        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
