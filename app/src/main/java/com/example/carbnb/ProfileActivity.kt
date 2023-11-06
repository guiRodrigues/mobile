package com.example.carbnb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.carbnb.databinding.ActivityProfileBinding
import com.example.carbnb.model.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var profileImage : ImageView
    private lateinit var backButton : ImageView
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button

    private lateinit var userIn : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        userIn = intent.getSerializableExtra("user") as User

        backButton = binding.gobackarrow
        profileImage = binding.profileImage
        username = binding.username
        email = binding.email
        password = binding.password
        saveButton = binding.saveChangesButton
        deleteButton = binding.deleteAccountButton
    }

    override fun onResume() {
        super.onResume()

        loadData()

        backButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        saveButton.setOnClickListener {
            setResult(RESULT_OK)
            val resultIntent = Intent()
            resultIntent.putExtra("username", username.text.toString())
            finish()
        }

        deleteButton.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to perform this action?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val resultIntent = Intent()
            resultIntent.putExtra("delete", true)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun loadData(){
        username.setText(userIn.name)
        email.setText(userIn.email)
        password.setText(userIn.password)
        if(userIn.profile != null) profileImage.setImageResource(userIn.profile!!)
    }
}