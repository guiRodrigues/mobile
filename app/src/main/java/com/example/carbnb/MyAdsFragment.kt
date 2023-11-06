package com.example.carbnb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carbnb.adapters.AdvertiseAdapter
import com.example.carbnb.adapters.MyAdvertisesAdapter
import com.example.carbnb.dao.AdvertisesDataSource
import com.example.carbnb.databinding.FragmentMyadsBinding
import com.example.carbnb.model.Advertise

class MyAdsFragment : Fragment(R.layout.fragment_myads) {

    private lateinit var binding : FragmentMyadsBinding

    private lateinit var recyclerView : RecyclerView
    private lateinit var createButton : Button

    private val dbUserAdvertises = ArrayList<Advertise>()
    private val dbAdvertises = AdvertisesDataSource.createAdvertisesList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMyadsBinding.bind(view)
        recyclerView = binding.recyclerViewAdvertises
        createButton = binding.createButton

        dbUserAdvertises.add(Advertise(
            1,
            "UserID",
            "Today",
            "BigBlack Fusca",
            "$200",
            "Angry Bulls Street, 34",
            "rent for weekends",
            R.drawable.bigblack_fusca,
            ArrayList()
        ))
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = MyAdvertisesAdapter(dbUserAdvertises){
            val code = verifyCode(it.carName)
            if(code == "delete") Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            else if (code == "viewOP"){
                val intent = Intent(requireContext(), MessagesActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(requireContext(), AdvertiseActivity::class.java)
                intent.putExtra("advertise", it)
                startActivity(intent)
            }
        }
    }

    private fun verifyCode(frase : String): String{
        val palavras = frase.split("\\s+".toRegex()) // Divide a string em palavras
        return if (palavras.isNotEmpty()) palavras.last() else ""
    }

    override fun onResume() {
        super.onResume()
        createButton.setOnClickListener {
            val intent = Intent(requireContext(), AdvertiseActivity::class.java)
            intent.putExtra("userID", dbAdvertises[0].ownerId)
            startActivity(intent)
        }
    }
}