package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterHobbies
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterMovie
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSkill
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSong
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageTwoBinding
import com.kodego.diangca.ebrahim.myslambook.model.*

class FormPageTwoFragment : Fragment() {

    private lateinit var binding: FragmentFormPageTwoBinding
    private lateinit var slamBook: SlamBook

    private lateinit var adapterSong: AdapterSong
    private var songs: ArrayList<Song> = ArrayList()

    private lateinit var adapterMovie: AdapterMovie
    private var movies: ArrayList<Movie> = ArrayList()

    private lateinit var adapterHobbies: AdapterHobbies
    private var hobbies: ArrayList<Hobbies> = ArrayList()

    private lateinit var adapterSkill: AdapterSkill
    private var skills: ArrayList<Skill> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slamBook = it.getParcelable("slamBooK")!!
            slamBook.printLog()
            restoreDataFromSlamBook()
        }
    }

    private fun restoreDataFromSlamBook() {
        songs = slamBook.favoriteSongs ?: ArrayList()
        movies = slamBook.favoriteMovies ?: ArrayList()
        hobbies = slamBook.hobbies ?: ArrayList()
        skills = slamBook.skillsWithRate ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFormPageTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            "slamBooKKey",
            viewLifecycleOwner
        ) { key, bundle ->
            bundle.getParcelable<SlamBook>("slamBooK")?.let {
                slamBook = it
                restoreDataFromSlamBook()
                if (::adapterSong.isInitialized) adapterSong.updateData(songs)
                if (::adapterMovie.isInitialized) adapterMovie.updateData(movies)
                if (::adapterHobbies.isInitialized) adapterHobbies.updateData(hobbies)
                if (::adapterSkill.isInitialized) adapterSkill.updateData(skills)
            }
        }
        arguments?.getParcelable<SlamBook>("slamBooK")?.let {
            slamBook = it
            restoreDataFromSlamBook()
        }
        initComponents()
    }

    private fun initComponents() {
        with(binding) {

            adapterSong = AdapterSong(root.context, songs)
            favSongList.layoutManager = LinearLayoutManager(root.context)
            favSongList.adapter = adapterSong

            adapterMovie = AdapterMovie(root.context, movies)
            favMovieList.layoutManager = LinearLayoutManager(root.context)
            favMovieList.adapter = adapterMovie

            adapterHobbies = AdapterHobbies(root.context, hobbies)
            hobbiesList.layoutManager = LinearLayoutManager(root.context)
            hobbiesList.adapter = adapterHobbies

            adapterSkill = AdapterSkill(root.context, skills)
            skillList.layoutManager = LinearLayoutManager(root.context)
            skillList.adapter = adapterSkill

            val rates = resources.getStringArray(R.array.skillRate)
            val rateAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rates)
            skillRate.setAdapter(rateAdapter)

            btnAddFavSong.setOnClickListener {
                btnAddOnClickListener(root, "Song", songName, favSongList)
            }
            btnAddFavMov.setOnClickListener {
                btnAddOnClickListener(root, "Movie", movieName, favMovieList)
            }
            btnAddHobbies.setOnClickListener {
                btnAddOnClickListener(root, "Hobbies", hobbiesInput, hobbiesList)
            }
            btnAddSkill.setOnClickListener {
                btnAddOnClickListener(root, "Skills", skillInput, skillList)
            }

            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnNext.setOnClickListener { btnNextOnClickListener() }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = Bundle()
                bundle.putParcelable("slamBooK", slamBook)
                findNavController().navigate(
                    R.id.action_formPageTwoFragment_to_formPageOneFragment,
                    bundle
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun btnAddOnClickListener(
        view: View?,
        type: String,
        field: TextInputEditText,
        recyclerView: RecyclerView
    ) {
        val text = field.text.toString().trim()
        if (text.isEmpty()) {
            field.error = "Please enter $type"
            Snackbar.make(binding.root, "Please check empty fields.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val duplicateExists = when (type) {
            "Song" -> songs.any { it.songName.equals(text, ignoreCase = true) }
            "Movie" -> movies.any { it.movieName.equals(text, ignoreCase = true) }
            "Hobbies" -> hobbies.any { it.hobbie.equals(text, ignoreCase = true) }
            "Skills" -> skills.any { it.skill.equals(text, ignoreCase = true) }
            else -> false
        }

        if (duplicateExists) {
            field.error = "$type already added"
            Snackbar.make(binding.root, "$type already added.", Snackbar.LENGTH_SHORT).show()
            return
        }

        when (type) {
            "Song" -> songs.add(Song(text))
            "Movie" -> movies.add(Movie(text))
            "Hobbies" -> hobbies.add(Hobbies(text))
            "Skills" -> {
                val selectedRate = binding.skillRate.text.toString()
                if (selectedRate.isEmpty()) {
                    binding.skillRate.error = "Please select a rate first"
                    Snackbar.make(
                        binding.root,
                        "Please select a rate first.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }
                skills.add(Skill(text, selectedRate.toInt()))
            }
        }

        field.error = null
        if (type == "Skills") binding.skillRate.error = null

        Snackbar.make(binding.root, "Data has been successfully added.", Snackbar.LENGTH_SHORT)
            .show()
        field.setText("")
        recyclerView.adapter?.notifyDataSetChanged()

        view?.let {
            val imm =
                binding.root.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            recyclerView.requestFocus()
        }
    }

    private fun btnNextOnClickListener() {

        var hasError = false

        if (songs.isEmpty()) {
            binding.songName.error = "Add at least one favorite song"
            hasError = true
        } else {
            binding.songName.error = null
        }

        if (movies.isEmpty()) {
            binding.movieName.error = "Add at least one favorite movie"
            hasError = true
        } else {
            binding.movieName.error = null
        }

        if (hobbies.isEmpty()) {
            binding.hobbiesInput.error = "Add at least one hobby"
            hasError = true
        } else {
            binding.hobbiesInput.error = null
        }

        if (skills.isEmpty()) {
            binding.skillInput.error = "Add at least one skill"
            binding.skillRate.error = "Select rate"
            hasError = true
        } else {
            binding.skillInput.error = null
            binding.skillRate.error = null
        }

        if (hasError) {
            Snackbar.make(
                binding.root,
                "Please fill in all sections before proceeding.",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        slamBook.favoriteSongs = songs
        slamBook.favoriteMovies = movies
        slamBook.hobbies = hobbies
        slamBook.skillsWithRate = skills

        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(
            R.id.action_formPageTwoFragment_to_formPageThreeFragment,
            bundle
        )
    }

    private fun btnBackOnClickListener() {
        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageOneFragment, bundle)
    }
}
