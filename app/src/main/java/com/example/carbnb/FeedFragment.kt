package com.example.carbnb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carbnb.adapters.AdvertiseAdapter
import com.example.carbnb.dao.AdvertisesDataSource
import com.example.carbnb.databinding.FragmentFeedBinding

class FeedFragment : Fragment(){

    private lateinit var binding: FragmentFeedBinding

    private lateinit var distanceButton : ImageView
    private lateinit var distanceKM : TextView
    private lateinit var seekBar: AppCompatSeekBar
    private lateinit var recyclerView: RecyclerView

    private var km = 0

    private val advertisesList = AdvertisesDataSource.createAdvertisesList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFeedBinding.bind(view)

        distanceButton = binding.locationButton
        distanceKM = binding.distanceKM

        recyclerView = binding.recyclerViewAdvertises
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = AdvertiseAdapter(advertisesList){
            val intent = Intent(requireContext(), SchedulingActivity::class.java)
            intent.putExtra("advertiseID", it.id.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        seekBar = binding.distanceSeekBar
        distanceButton.setOnClickListener {
            seekBar.visibility = View.VISIBLE
        }

        initSeekbar()
    }

    private fun initSeekbar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var string = seekBar?.progress.toString() + "KM"
                distanceKM.text = string
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                km = seekBar!!.progress
                seekBar.visibility = View.GONE
            }

        })

    }
}