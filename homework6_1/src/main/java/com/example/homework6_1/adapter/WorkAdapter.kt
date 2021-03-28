package com.example.homework6_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.homework6_1.R
import com.example.homework6_1.data.Work

typealias OnWorkClickListener = (work: Work, position: Int) -> Unit

class WorkAdapter(
        var workList: ArrayList<Work>,
        var onWorkClickListener: OnWorkClickListener
        ) : RecyclerView.Adapter<WorkAdapter.WorkViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_works_list, parent, false)
        return WorkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val work = workList[position]
        holder.bind(work, onWorkClickListener)
    }

    override fun getItemCount(): Int {
        return workList.size
    }

    class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val workName: TextView = itemView.findViewById(R.id.nameWorkInWorkList)
        private val cost: TextView = itemView.findViewById(R.id.costInWorkList)
        private val date: TextView = itemView.findViewById(R.id.dateWorkInWorkList)
        private val progressWork: TextView = itemView.findViewById(R.id.textStatusInWorkList)
        private val statusWorkImage: ImageView = itemView.findViewById(R.id.statusWorkInWorkList)

        fun bind(work: Work, onWorkClickListener: OnWorkClickListener) {
            workName.text = work.workName
            cost.text = work.cost.toString()
            date.text = work.applicationDate
            progressWork.text = work.progressWork
            statusWorkImage.setColorFilter(ContextCompat.getColor(itemView.context, work.colorStatus!!))

            itemView.setOnClickListener {
                onWorkClickListener.invoke(work, adapterPosition)
            }
        }
    }
}