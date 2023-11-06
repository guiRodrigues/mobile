package com.example.carbnb

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.util.Pair

import com.example.carbnb.dao.AdvertisesDataSource
import com.example.carbnb.dao.UsersDataSource
import com.example.carbnb.databinding.ActivityScheduleBinding
import com.example.carbnb.model.Advertise

import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchedulingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScheduleBinding

    private lateinit var backButton : ImageView
    private lateinit var carName : TextView
    private lateinit var carPicture : ImageView
    private lateinit var mapsButton : Button
    private lateinit var locationText : TextView
    private lateinit var description : TextView
    private lateinit var owner : TextView
    private lateinit var dateButton : Button
    private lateinit var userMessage : EditText
    private lateinit var confirmButton : Button
    private lateinit var cancelButton : Button

    private lateinit var advertiseID : String
    private lateinit var advertise: Advertise
    private val dbAdvertises = AdvertisesDataSource.createAdvertisesList()
    private val dbUsers = UsersDataSource.createUsersList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        advertiseID = intent.getStringExtra("advertiseID") as String
        advertise = dbAdvertises.find{ advertiseID == it.id.toString()}!!

        backButton = binding.gobackarrow
        carName = binding.carName
        carPicture = binding.carPicture
        mapsButton = binding.mapsButton
        locationText = binding.location
        description = binding.descriptionText
        owner = binding.owner
        dateButton = binding.calendarButton
        userMessage = binding.userMessage
        confirmButton = binding.confirmButton
        cancelButton = binding.cancelButton

    }

    override fun onStart() {
        super.onStart()

        carName.text = advertise.carName
        if (advertise.carImage != null) carPicture.setImageResource(advertise.carImage!!)
        locationText.text = advertise.location
        description.text = advertise.description
        owner.text = dbUsers.find { advertise.ownerId == it.id }!!.name

    }

    override fun onResume() {
        super.onResume()
        backButton.setOnClickListener { finish() }

        mapsButton.setOnClickListener {
            requestLocalPermissions()
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

        }
        confirmButton.setOnClickListener {
            Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show()
            finish()
        }
        cancelButton.setOnClickListener { finish() }
        dateButton.setOnClickListener {
            val calendar = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                ).build()

            calendar.show(supportFragmentManager, "TAG")

            calendar.addOnPositiveButtonClickListener {
                val date1 = convertTimeToDate(it.first)
                val date2 = convertTimeToDate(it.second)
                val period = "$date1 to $date2"
                dateButton.text = period
            }
            calendar.addOnNegativeButtonClickListener { calendar.dismiss() }
        }
    }

    private fun requestLocalPermissions(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
        }
    }
    private fun convertTimeToDate(timeInMilliseconds: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timeInMilliseconds)
        return dateFormat.format(date)
    }

}