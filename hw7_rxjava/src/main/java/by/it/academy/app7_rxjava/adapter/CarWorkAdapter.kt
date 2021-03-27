package by.it.academy.app7_rxjava.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app7_rxjava.R
import by.it.academy.app7_rxjava.entity.WorkItem
import java.util.Locale
import kotlin.collections.ArrayList

class CarWorkAdapter(private var values: MutableList<WorkItem>) : RecyclerView.Adapter<CarWorkAdapter.WorkItemViewHolder>(), Filterable {


    private val listOfValuesForFilter: ArrayList<WorkItem> = ArrayList(values)

    lateinit var onWorkItemClickListener: (workItem: WorkItem) -> Unit

    class WorkItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val applicationDate: TextView = itemView.findViewById(R.id.textViewDateAdded)
        val textViewWorkType: TextView = itemView.findViewById(R.id.textViewWorkType)
        val textViewWorkPrice: TextView = itemView.findViewById(R.id.textViewWorkPrice)
        val imageViewWorkStatus: ImageView = itemView.findViewById(R.id.imageViewWorkStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return WorkItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkItemViewHolder, position: Int) {

        val workItem = values[position]

        with(holder) {
            itemView.setOnClickListener { onWorkItemClickListener.invoke(workItem) }
            applicationDate.text = workItem.applicationDate
            textViewWorkType.text = workItem.workType
            val cost = "$" + workItem.cost
            textViewWorkPrice.text = cost
            imageViewWorkStatus.setImageResource(
                    when (workItem.workStatus) {
                        1 -> R.drawable.ic_pending
                        2 -> R.drawable.ic_in_progress
                        3 -> R.drawable.ic_completed
                        else -> 0
                    }
            )
        }

    }

    override fun getItemCount(): Int = values.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = mutableListOf<WorkItem>()

                if (constraint.isNullOrEmpty()) {
                    result.addAll(listOfValuesForFilter)

                } else {
                    val enteredChars = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    listOfValuesForFilter.forEach {

                        if (it.workType.toLowerCase(Locale.getDefault()).trim().contains(enteredChars)) {
                            result.add(it)
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = result
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                values = results?.values as MutableList<WorkItem>
                notifyDataSetChanged()

            }
        }
    }
}