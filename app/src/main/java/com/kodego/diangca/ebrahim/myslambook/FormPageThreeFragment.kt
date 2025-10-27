package com.kodego.diangca.ebrahim.myslambook

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageThreeBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import java.io.File
import java.io.FileOutputStream

class FormPageThreeFragment : Fragment() {

    private lateinit var slamBook: SlamBook
    private lateinit var binding: FragmentFormPageThreeBinding

    private var cameraImageUri: Uri? = null
    private var selectedImageUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                selectedImageUri = cameraImageUri
                selectedImageUri?.let { uri -> saveAndLoadImage(uri) }
                Toast.makeText(requireContext(), "Profile picture taken!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Camera cancelled.", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data
                selectedImageUri?.let { uri -> saveAndLoadImage(uri) }
                Toast.makeText(requireContext(), "Profile picture selected!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slamBook = it.getParcelable("slamBooK")!!
            slamBook.printLog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormPageThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()

        binding.btnBrowse.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { openCamera() }

        slamBook.profilePic?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .placeholder(R.drawable.sc_profile)
                .into(binding.profileImageView)
        }
    }

    private fun initComponents() {
        with(binding) {
            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnSave.setOnClickListener { btnSaveOnClickListener() }
        }
    }

    private fun saveAndLoadImage(uri: Uri) {
        val savedUri = saveImageToInternalStorage(uri)
        slamBook.profilePic = savedUri.toString()

        Glide.with(this)
            .load(savedUri)
            .placeholder(R.drawable.sc_profile)
            .into(binding.profileImageView)
    }

    private fun btnSaveOnClickListener() {
        with(binding) {
            if (describeMe.text.isNullOrEmpty() ||
                defineLove.text.isNullOrEmpty() ||
                defineFriendship.text.isNullOrEmpty() ||
                adviceForMe.text.isNullOrEmpty()
            ) {
                if (describeMe.text.isNullOrEmpty()) describeMe.error = "Please enter Nickname"
                if (defineLove.text.isNullOrEmpty()) defineLove.error = "Please enter Friend call you"
                if (defineFriendship.text.isNullOrEmpty()) defineFriendship.error = "Please enter Like to call you"
                if (adviceForMe.text.isNullOrEmpty()) adviceForMe.error = "Please enter First name"

                Snackbar.make(binding.root, "Please check empty fields", Snackbar.LENGTH_SHORT).show()
                return
            }

            slamBook.defineLove = defineLove.text.toString()
            slamBook.describeMe = describeMe.text.toString()
            slamBook.defineFriendship = defineFriendship.text.toString()
            slamBook.memorableExperience = memorableExperience.text.toString()
            slamBook.adviceForMe = adviceForMe.text.toString()
            slamBook.rateMe = ratingBar.rating.toInt()

            // Send updated slamBook back to FormPageTwoFragment
            parentFragmentManager.setFragmentResult(
                "slamBooKKey",
                Bundle().apply { putParcelable("slamBooK", slamBook) }
            )

            Toast.makeText(binding.root.context, "Saving..", Toast.LENGTH_LONG).show()
            slamBook.printLog()

            val intent = Intent(requireContext(), MenuActivity::class.java)
            intent.putExtra("slamBooK", slamBook)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun btnBackOnClickListener() {
        // Navigate back with slamBook in bundle
        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(
            R.id.action_formPageThreeFragment_to_formPageTwoFragment,
            bundle
        )
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun openCamera() {
        val contentValues = ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.TITLE, "New Profile Picture")
            put(android.provider.MediaStore.Images.Media.DESCRIPTION, "From Camera")
        }

        cameraImageUri = requireContext().contentResolver.insert(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        takePictureLauncher.launch(cameraImageUri)
    }

    private fun saveImageToInternalStorage(uri: Uri): Uri {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            @Suppress("DEPRECATION")
            android.provider.MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        val filename = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, filename)
        FileOutputStream(file).use { fos ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, fos)
        }

        return Uri.fromFile(file)
    }
}
