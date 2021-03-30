package by.it.academy.app8_2task.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.entity.PlaceItem
import java.util.Locale
import kotlin.collections.ArrayList

class PlaceAdapter(private var values: MutableList<PlaceItem>) : RecyclerView.Adapter<PlaceAdapter.CarItemViewHolder>(), Filterable {
    private val listOfValuesForFilter: ArrayList<PlaceItem> = ArrayList(values)
    lateinit var onEditClickListener: (placeItem: PlaceItem) -> Unit
    lateinit var onPlaceItemClickListener: (placeItem: PlaceItem) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return CarItemViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int) {
        val placeItem = values[position]
        with(holder) {
            editPlaceInfo.setOnClickListener { onEditClickListener.invoke(placeItem) }
            itemView.setOnClickListener { onPlaceItemClickListener.invoke(placeItem) }
            placeName.text = placeItem.placeName
            placeFishingType.text = placeItem.fishingType
            fishSpecies.text = placeItem.fishSpecies
            placeLocality.text = placeItem.locality
            placePhoto.setImageURI(placeItem.pathImage?.toUri())
        }
    }
    override fun getItemCount(): Int = values.size
    class CarItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        val placeFishingType: TextView = itemView.findViewById(R.id.placeFishingType)
        val fishSpecies: TextView = itemView.findViewById(R.id.fishSpecies)
        val placeLocality: TextView = itemView.findViewById(R.id.placeLocality)
        val editPlaceInfo: ImageButton = itemView.findViewById(R.id.editPlaceInfo)
        val placePhoto: ImageView = itemView.findViewById(R.id.placePhoto)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = mutableListOf<PlaceItem>()
                if (constraint.isNullOrEmpty()) {
                    result.addAll(listOfValuesForFilter)
                } else {
                    val enteredChars = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    listOfValuesForFilter.forEach {
                        if (it.locality.toLowerCase(Locale.getDefault()).trim().contains(enteredChars) ||
                                it.fishSpecies.toLowerCase(Locale.getDefault()).trim().contains(enteredChars)) {
                            result.add(it)
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = result
                return filterResult
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                values = results?.values as MutableList<PlaceItem>
                notifyDataSetChanged()
            }
        }
    }
}