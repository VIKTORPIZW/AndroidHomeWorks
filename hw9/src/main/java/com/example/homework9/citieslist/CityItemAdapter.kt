package com.example.homework9.citieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework9.R

class CityItemAdapter(private val cityItemsList: List<CityItem>,
                      private val listener: OnItemClickListener):
        RecyclerView.Adapter<CityItemAdapter.CityItemVewHolder>() {

    inner class CityItemVewHolder(itemVew: View): RecyclerView.ViewHolder(itemVew), View.OnClickListener{

        private val cityCountry: TextView = itemVew.findViewById(R.id.city_item)
        private val iconCheck: ImageView = itemVew.findViewById(R.id.check_icon_city_item)

        fun bind(cityItem: CityItem){
            cityCountry.text = cityItem.city
        }

        init {
            itemView.setOnClickListener (this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemVewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.city_item,parent,false)
        return CityItemVewHolder(view)
    }

    override fun onBindViewHolder(holder: CityItemVewHolder, position: Int) {
        holder.bind(cityItemsList.get(position))
    }

    override fun getItemCount(): Int {
        return cityItemsList.size
    }
}