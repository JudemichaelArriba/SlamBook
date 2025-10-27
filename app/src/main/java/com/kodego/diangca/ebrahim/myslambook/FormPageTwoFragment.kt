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

        // Listeniner for updated slamBook from FormPageThreeFragment
        parentFragmentManager.setFragmentResultListener("slamBooKKey", viewLifecycleOwner) { key, bundle ->
            bundle.getParcelable<SlamBook>("slamBooK")?.let {
                slamBook = it
                restoreDataFromSlamBook()

                // Refresher for adapters
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
            // Initialize adapters
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

            // Setup skill rate dropdown
            val rates = resources.getStringArray(R.array.skillRate)
            val rateAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rates)
            skillRate.setAdapter(rateAdapter)



            // Set button click listeners
            btnAddFavSong.setOnClickListener {
                btnAddOnClickListener(root, "Song", songName, favSongList)
            }
            btnAddFavMov.setOnClickListener {
                btnAddOnClickListener(root, "Movie", movieName, favMovieList)
            }
            btnAddHobbies.setOnClickListener {
                btnAddOnClickListener(root, "Hobbies", hobbiesInput, hobbiesList) // ✅ corrected
            }
            btnAddSkill.setOnClickListener {
                btnAddOnClickListener(root, "Skills", skillInput, skillList) // ✅ corrected
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
        val text = field.text.toString()
        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Please check empty fields.", Snackbar.LENGTH_SHORT).show()
            return
        }
        when (type) {
            "Song" -> songs.add(Song(text))
            "Movie" -> movies.add(Movie(text))
            "Hobbies" -> hobbies.add(Hobbies(text))
            "Skills" -> {
                val selectedRate = binding.skillRate.text.toString()
                if (selectedRate.isEmpty()) {
                    Snackbar.make(binding.root, "Please select a rate first.", Snackbar.LENGTH_SHORT).show()
                    return
                }
                skills.add(Skill(text, selectedRate.toInt()))
            }
        }
        Snackbar.make(binding.root, "Data has been successfully added.", Snackbar.LENGTH_SHORT).show()
        field.setText("")
        recyclerView.adapter?.notifyDataSetChanged()

        view?.let {
            val imm = binding.root.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            recyclerView.requestFocus()
        }
    }

    private fun btnNextOnClickListener() {
        slamBook.favoriteSongs = songs
        slamBook.favoriteMovies = movies
        slamBook.hobbies = hobbies
        slamBook.skillsWithRate = skills

        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageThreeFragment, bundle)
    }

    private fun btnBackOnClickListener() {
        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageOneFragment, bundle)
    }
}
