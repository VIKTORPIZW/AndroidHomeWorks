package by.it.academy.myFishingPlaces.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem
import java.util.Locale
import kotlin.collections.ArrayList

class PlaceDescriptionAdapter(private var values: MutableList<PlaceDescriptionItem>) : RecyclerView.Adapter<PlaceDescriptionAdapter.PlaceDescriptionItemViewHolder>(), Filterable {


    private val listOfValuesForFilter: ArrayList<PlaceDescriptionItem> = ArrayList(values)

    lateinit var onFishDescriptionItemClickListener: (placeDescriptionItem: PlaceDescriptionItem) -> Unit

    class PlaceDescriptionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val applicationDate: TextView = itemView.findViewById(R.id.textViewDateAdded)
        val textViewRoadToWater: TextView = itemView.findViewById(R.id.textViewPlaceDescriptionType)
        val textViewPlaceDescriptionDistance: TextView = itemView.findViewById(R.id.textViewPlaceDescriptionDistance)
        val imageViewFishingStatus: ImageView = itemView.findViewById(R.id.imageViewFishingStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDescriptionItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return PlaceDescriptionItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceDescriptionItemViewHolder, position: Int) {

        val workItem = values[position]

        with(holder) {
            itemView.setOnClickListener { onFishDescriptionItemClickListener.invoke(workItem) }
            applicationDate.text = workItem.applicationDate
            textViewRoadToWater.text = workItem.roadToWater
            val cost = "КМ" + workItem.distance
            textViewPlaceDescriptionDistance.text = cost
            imageViewFishingStatus.setImageResource(
                    when (workItem.fishingStatus) {
                        1 -> R.drawable.molluscs
                        2 -> R.drawable.fish2
                        3 -> R.drawable.fish1
                        else -> 0
                    }
            )
        }

    }

    override fun getItemCount(): Int = values.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = mutableListOf<PlaceDescriptionItem>()

                if (constraint.isNullOrEmpty()) {
                    result.addAll(listOfValuesForFilter)

                } else {
                    val enteredChars = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    listOfValuesForFilter.forEach {

                        if (it.roadToWater.toLowerCase(Locale.getDefault()).trim().contains(enteredChars)) {
                            result.add(it)
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = result
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                values = results?.values as MutableList<PlaceDescriptionItem>
                notifyDataSetChanged()

            }
        }
    }
}