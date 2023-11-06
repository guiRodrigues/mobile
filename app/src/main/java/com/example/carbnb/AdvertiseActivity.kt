package com.example.carbnb

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.carbnb.dao.AdvertisesDataSource
import com.example.carbnb.databinding.ActivityAdvertiseBinding
import com.example.carbnb.model.Advertise
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.random.Random

class AdvertiseActivity : AppCompatActivity() {

    companion object{
        private const val GALLERY_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private lateinit var binding : ActivityAdvertiseBinding

    private lateinit var carImg : ImageView
    private lateinit var carName : TextInputEditText
    private lateinit var price : TextInputEditText
    private lateinit var description : TextInputEditText
    private lateinit var location : TextInputEditText
    private lateinit var postButton : Button

    private var createAd = false
    private lateinit var ownerID : String
    private lateinit var advertise: Advertise
    private lateinit var dialog : AlertDialog
    private lateinit var mImageURI : Uri

    private val dbAdvertises = AdvertisesDataSource.createAdvertisesList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifyOperation()

        carImg = binding.carPicture
        carName = binding.carName
        description = binding.descriptionText
        price = binding.price
        location = binding.locationText
        postButton = binding.confirmButton
    }

    private fun verifyOperation(){
        if (intent.getStringExtra("userID") != null){
            ownerID = intent.getStringExtra("userID") as String
            createAd = true
        }
        else if(@Suppress("DEPRECATION") intent.getSerializableExtra("advertise") != null){
            @Suppress("DEPRECATION")
            advertise = intent.getSerializableExtra("advertise") as Advertise
        }
    }

    override fun onStart() {
        super.onStart()
        if (!createAd) {
            if (advertise.carImage != null) carImg.setImageResource(advertise.carImage!!)
            carName.setText(advertise.carName)
            description.setText(advertise.description)
            price.setText(advertise.price)
            location.setText(advertise.location)
        }
    }
    override fun onResume() {
        super.onResume()

        binding.gobackarrow.setOnClickListener { finish() }

        carImg.setOnClickListener {
            checkPermissionGallery()
        }

        postButton.setOnClickListener {
            if (nullVerifier()){
                //dbAdvertises.add(createAdvertise())
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun createAdvertise(): Advertise {
        return Advertise(
            Random.nextInt(1000),
            ownerID,
            SimpleDateFormat.getDateInstance()
                .format(Calendar.getInstance().time),
            carName.text.toString(),
            price.text.toString(),
            location.text.toString(),
            description.text.toString(),
            null,
            ArrayList()
        )
    }

    private fun checkPermissionGallery(){
        val granted = checkPermission(GALLERY_PERMISSION)

        when {
            granted ->  resultGallery.launch(
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))

            shouldShowRequestPermissionRationale(GALLERY_PERMISSION) -> showDialogPermission()

            else -> requestGallery.launch(GALLERY_PERMISSION)
        }
    }

    private fun checkPermission(permission : String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.data?.data != null) {
                mImageURI = result.data?.data as Uri
                carImg.setImageURI(mImageURI)
            }
        }

    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ permission ->
            if (permission){
                resultGallery.launch(
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }else{
                showDialogPermission()
            }
        }
    private fun showDialogPermission(){
        val builder = AlertDialog.Builder(this)
            .setTitle("Request").setMessage("we need permission to access this smartphone gallery")
            .setNegativeButton("decline") { _,_ -> dialog.dismiss()}
            .setPositiveButton("accept") { _,_ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()
    }
    private fun nullVerifier(): Boolean {
        val fieldsToCheck = listOf(
            carName to "Car model",
            price to "Price",
            location to "Location"
        )

        for ((field, message) in fieldsToCheck) {
            if (field.text.isNullOrEmpty()) {
                emptyMessage(message)
                return false
            }
        }
        return true
    }

    private fun emptyMessage(input : String){
        Toast.makeText(this, "$input is empty" , Toast.LENGTH_SHORT).show()
    }
}