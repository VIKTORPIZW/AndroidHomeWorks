package com.example.homework6_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app6_1task.R

class WorkItemAdapter(private val workEntityList: List<WorkItem>):
                        RecyclerView.Adapter<WorkItemAdapter.WorkItemViewHolder>() {

   inner class WorkItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val workStatus: TextView = itemView.findViewById(R.id.working_progress_text_item)
        private val workName : TextView = itemView.findViewById(R.id.work_name_item)
        private val workDate : TextView = itemView.findViewById(R.id.work_date_item)
        private val workPrice : TextView = itemView.findViewById(R.id.work_price_item)

        fun bind(workItem: WorkItem){
            workStatus.text = workItem.workStatus
            workName.text = workItem.workName
            workDate.text = workItem.workDate
            workPrice.text = workItem.workCost
        }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return WorkItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkItemViewHolder, position: Int) {
        val item = workEntityList[position]
        val pending = holder.itemView.context.getString(R.string.work_pending)
        val progress = holder.itemView.context.getString(R.string.work_in_progress)
        val completed = holder.itemView.context.getString(R.string.work_completed)
        if (item.workStatus.equals(pending)){
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).setTextColor(holder.itemView.context.getColor(R.color.red))
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).
            setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_construction_pending24, 0, 0)
        }
        if (item.workStatus.equals(progress)){
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).setTextColor(holder.itemView.context.getColor(R.color.dark_yellow))
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).
            setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_construction_progress24, 0, 0)
        }
        if (item.workStatus.equals(completed)){
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).setTextColor(holder.itemView.context.getColor(R.color.green))
            holder.itemView.findViewById<TextView>(R.id.working_progress_text_item).
            setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_construction_completed24, 0, 0)
        }
        holder.bind(workEntityList.get(position))
    }

    override fun getItemCount(): Int {
        return workEntityList.size
    }
}