package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageOneBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import java.text.SimpleDateFormat
import java.util.*

class FormPageOneFragment : Fragment() {

    private lateinit var binding: FragmentFormPageOneBinding
    private lateinit var slamBook: SlamBook
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFormPageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        if (arguments != null) {
            slamBook = (arguments?.getParcelable("slamBooK") as SlamBook?)!!
            slamBook.printLog()
            restoreField()
        } else {
            slamBook = SlamBook()
        }


        binding.contactNo.addTextChangedListener(object : TextWatcher {
            private var editing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editing) return
                editing = true

                var input = s.toString()


                if (input.startsWith("0")) {
                    input = input.drop(1)
                    binding.contactNo.setText(input)
                    binding.contactNo.setSelection(input.length)
                }


                if (input.length > 10) {
                    input = input.substring(0, 10)
                    binding.contactNo.setText(input)
                    binding.contactNo.setSelection(input.length)
                }

                editing = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        with(binding) {
            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnNext.setOnClickListener { btnNextOnClickListener() }

            dateInput.setOnClickListener { showDatePicker() }

            val genderItems = resources.getStringArray(R.array.gender)
            val genderAdapter = ArrayAdapter(requireContext(), R.layout.list_item, genderItems)
            gender.setAdapter(genderAdapter)

            val statusItems = resources.getStringArray(R.array.status)
            val statusAdapter = ArrayAdapter(requireContext(), R.layout.list_item, statusItems)
            status.setAdapter(statusAdapter)
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.dateInput.setText(dateFormat.format(selectedDate.time))
            }, year, month, day
        )
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun restoreField() {
        binding.apply {
            nickName.setText(slamBook.nickName)
            friendCall.setText(slamBook.friendCallMe)
            likeToCall.setText(slamBook.likeToCallMe)
            lastName.setText(slamBook.lastName)
            firstName.setText(slamBook.firstName)

            if (!slamBook.birthDate.isNullOrEmpty()) {
                dateInput.setText(slamBook.birthDate)
            }

            if (!slamBook.gender.isNullOrEmpty()) {
                gender.setText(slamBook.gender, false)
            }

            if (!slamBook.status.isNullOrEmpty()) {
                status.setText(slamBook.status, false)
            }

            emailAdd.setText(slamBook.email)
            contactNo.setText(slamBook.contactNo?.replace("+63", "") ?: "")
            address.setText(slamBook.address)
        }
    }

    private fun btnNextOnClickListener() {
        with(binding) {
            if (nickName.text.isNullOrEmpty() || friendCall.text.isNullOrEmpty() || likeToCall.text.isNullOrEmpty() || lastName.text.isNullOrEmpty() || firstName.text.isNullOrEmpty() || dateInput.text.isNullOrEmpty() || gender.text.isNullOrEmpty() || status.text.isNullOrEmpty() || emailAdd.text.isNullOrEmpty() || contactNo.text.isNullOrEmpty() || address.text.isNullOrEmpty()) {
                if (nickName.text.isNullOrEmpty()) nickName.error = "Please enter Nickname"
                if (friendCall.text.isNullOrEmpty()) friendCall.error =
                    "Please enter Friend call you"
                if (likeToCall.text.isNullOrEmpty()) likeToCall.error =
                    "Please enter Like to call you"
                if (lastName.text.isNullOrEmpty()) lastName.error = "Please enter Last name"
                if (firstName.text.isNullOrEmpty()) firstName.error = "Please enter First name"
                if (dateInput.text.isNullOrEmpty()) dateInput.error = "Please select birthdate"
                if (emailAdd.text.isNullOrEmpty()) emailAdd.error = "Please enter Email"
                if (contactNo.text.isNullOrEmpty()) contactNo.error = "Please enter Contact No."
                if (address.text.isNullOrEmpty()) address.error = "Please enter Address"

                Snackbar.make(binding.root, "Please check empty fields", Snackbar.LENGTH_SHORT)
                    .show()
                return
            }

            val email = emailAdd.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailAdd.error = "Please enter a valid Email address"
                Snackbar.make(binding.root, "Invalid email format", Snackbar.LENGTH_SHORT).show()
                return
            }

            var contact = contactNo.text.toString()
            if (contact.length < 10) {
                contactNo.error = "Please enter a valid contact number"
                Snackbar.make(binding.root, "Invalid contact number", Snackbar.LENGTH_SHORT).show()
                return
            }


            val countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus
            if (contact.startsWith("0")) {
                contact = countryCode + contact.drop(1)
            } else if (!contact.startsWith("+")) {
                contact = countryCode + contact
            }

            slamBook.nickName = nickName.text.toString()
            slamBook.friendCallMe = friendCall.text.toString()
            slamBook.likeToCallMe = likeToCall.text.toString()
            slamBook.lastName = lastName.text.toString()
            slamBook.firstName = firstName.text.toString()
            slamBook.birthDate = dateInput.text.toString()
            slamBook.gender = gender.text.toString()
            slamBook.status = status.text.toString()
            slamBook.email = emailAdd.text.toString()
            slamBook.contactNo = contact
            slamBook.address = address.text.toString()

            Log.d("FORM 1", "Saved Data: ${slamBook.birthDate}")

            val bundle = Bundle()
            bundle.putParcelable("slamBooK", slamBook)
            findNavController().navigate(
                R.id.action_formPageOneFragment_to_formPageTwoFragment, bundle
            )
        }
    }

    private fun btnBackOnClickListener() {
        val intent = Intent(requireContext(), MenuActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
