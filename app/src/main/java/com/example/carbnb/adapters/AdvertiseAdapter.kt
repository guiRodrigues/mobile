package com.example.carbnb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carbnb.databinding.ItemAdvertiseBinding
import com.example.carbnb.model.Advertise

class AdvertiseAdapter(private val advertisesList: MutableList<Advertise>, private val onItemClicked: (Advertise)->Unit):
    RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val advertiseItemView = ItemAdvertiseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AdvertiseViewHolder(advertiseItemView)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val advertise = advertisesList[position]
        holder.apply {
            if (advertise.carImage != null) carImage.setImageResource(advertise.carImage!!)
            local.text = advertise.location
            description.text = advertise.description
            price.text = advertise.price
            layout.setOnClickListener {
                onItemClicked(advertise)
            }
        }
    }

    override fun getItemCount(): Int {
        return advertisesList.size
    }

    class AdvertiseViewHolder(binding: ItemAdvertiseBinding): RecyclerView.ViewHolder(binding.root){
        val carImage = binding.carImage
        val local = binding.localText
        val description = binding.descriptionText
        val price = binding.price
        val layout = binding.layout
    }
}