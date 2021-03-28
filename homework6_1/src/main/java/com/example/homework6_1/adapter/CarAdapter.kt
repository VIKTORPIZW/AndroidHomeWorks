package com.example.homework6_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework6_1.R
import com.example.homework6_1.data.Car

typealias OnCarClickListener = (car: Car, position: Int, operation: Int) -> Unit
private const val OPERATION_EDIT_CAR: Int = 1
private const val OPERATION_WORK_ADD: Int = 2

class CarAdapter(
        var carList: ArrayList<Car>,
        var onCarClickListener: OnCarClickListener
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cars_list, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.bind(car, onCarClickListener)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var registerNumber: TextView = itemView.findViewById(R.id.numberCar)
        private var producerAuto: TextView = itemView.findViewById(R.id.producerAuto)
        private var modelAuto: TextView = itemView.findViewById(R.id.modelAuto)
        private var owner:TextView = itemView.findViewById(R.id.nameMaster)
        private var photoCar: ImageView = itemView.findViewById(R.id.imageViewCar)
        private var buttonEditCarMainActivity: ImageButton = itemView.findViewById(R.id.buttonClickEditCar)

        fun bind(car: Car, onCarClickListener: OnCarClickListener) {
            registerNumber.text = car.registerNumber
            producerAuto.text = car.producer
            modelAuto.text = car.model
            owner.text = car.nameOwner
            if(car.photo == null) {
                photoCar.setImageResource(R.drawable.ic_baseline_photo_camera_24)
            } else {
                Glide.with(itemView).load(car.photo).into(photoCar)
            }
            buttonEditCarMainActivity.setOnClickListener {
                onCarClickListener.invoke(car, adapterPosition, OPERATION_EDIT_CAR)
            }
            itemView.setOnClickListener {
                onCarClickListener.invoke(car, adapterPosition, OPERATION_WORK_ADD)
            }
        }
    }
}