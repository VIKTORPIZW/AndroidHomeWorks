package by.it.academy.app6_1task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarWorkAdapter(private val values: MutableList<WorkItem>) : RecyclerView.Adapter<CarWorkAdapter.WorkViewHolder>() {

    class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val applicationDate: TextView = itemView.findViewById(R.id.textViewApplicationDate)
        val textViewWorkType: TextView = itemView.findViewById(R.id.textViewWorkType)
        val textViewWorkPrice: TextView = itemView.findViewById(R.id.textViewWorkPrice)
        val imageViewWorkStatus: ImageView = itemView.findViewById(R.id.imageViewWorkStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.work_item, parent, false)
        return WorkViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val workItem = values[position]

        with(holder) {
            applicationDate.text = workItem.applicationDate
            val cost = "$ ${workItem.cost}"
            textViewWorkPrice.text = cost
            textViewWorkType.text = workItem.workType
            imageViewWorkStatus.setImageResource(
                    when (workItem.workStatus) {
                        1 -> R.drawable.ic_pending
                        2 -> R.drawable.ic_in_progress
                        3 -> R.drawable.ic_completed
                        else -> 0
                    })
        }

    }

    override fun getItemCount(): Int = values.size
}