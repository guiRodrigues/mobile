package com.example.carbnb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.carbnb.R
import com.example.carbnb.databinding.FragmentSingInBinding
import com.example.carbnb.HomeActivity
import com.example.carbnb.dao.UsersDataSource
import com.example.carbnb.model.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

class SingInFragment : Fragment(R.layout.fragment_sing_in) {

    private lateinit var binding : FragmentSingInBinding

    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var connection : CheckBox
    private lateinit var singinButton : Button

    //private val auth = FirebaseAuth.getInstance()

    private val usersList : MutableList<User> = UsersDataSource.createUsersList()
    private lateinit var userIn : User;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingInBinding.bind(view)

        email = binding.emailTxt
        password = binding.passwordTxt
        connection = binding.connection
        singinButton = binding.singinButton
    }

    override fun onResume() {
        super.onResume()
        singinButton.setOnClickListener{
            if (nullVerify()){
                if(findUser() != null) executeLogin()
                //login(email.text.toString(),password.text.toString())
            }
        }
    }

    /*private fun login(email: String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authentication ->
                if (authentication.isSuccessful){
                    executeLogin()
                }
            }.addOnFailureListener { exception ->
                throwErrorMessage(exception)
            }
    }*/

    private fun throwErrorMessage(exception: Exception){
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException -> "There is no User recorded to this Email"
            is FirebaseAuthInvalidCredentialsException -> "Invalid password"
            is FirebaseNetworkException -> "No Internet Connection"
            else -> "Error"
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun executeLogin(){
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("user", userIn)
        requireActivity().apply {
            startActivity(intent)
            finish()
        }
    }

    private fun nullVerify(): Boolean{
        if (email.text.isNullOrEmpty() || password.text.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Fill all the camps", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }



    private fun findUser() : User?{
        val user = usersList.find { email.text.toString() == it.email }
        return if (user == null ) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            null
        } else {
            return if (password.text.toString() != user.password){
                    Toast.makeText(requireContext(),"Wrong password", Toast.LENGTH_SHORT).show()
                    null
                }
                else {
                    userIn = user
                    user
                }
        }
    }

}
