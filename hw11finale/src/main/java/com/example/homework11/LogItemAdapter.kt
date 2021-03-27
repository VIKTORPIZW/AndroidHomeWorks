package com.example.homework11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogItemAdapter(private val logItemsList: List<LogItem>):
        RecyclerView.Adapter<LogItemAdapter.LogItemViewHolder>(){

    class LogItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val date: TextView = itemView.findViewById(R.id.item_date)
        private val time: TextView = itemView.findViewById(R.id.item_time)
        private val event: TextView = itemView.findViewById(R.id.item_event)

        fun bind(logItem: LogItem){
            date.text = logItem.date
            time.text = logItem.time
            event.text = logItem.event
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.log_item,parent,false)
        return LogItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogItemViewHolder, position: Int) {
        holder.bind(logItemsList[position])
    }

    override fun getItemCount(): Int = logItemsList.size
}