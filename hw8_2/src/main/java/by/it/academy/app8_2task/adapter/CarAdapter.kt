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
import by.it.academy.app8_2task.entity.CarItem
import java.util.Locale
import kotlin.collections.ArrayList

class CarAdapter(private var values: MutableList<CarItem>) : RecyclerView.Adapter<CarAdapter.CarItemViewHolder>(), Filterable {

    private val listOfValuesForFilter: ArrayList<CarItem> = ArrayList(values)

    lateinit var onEditClickListener: (carItem: CarItem) -> Unit
    lateinit var onCarItemClickListener: (carItem: CarItem) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int) {

        val carItem = values[position]

        with(holder) {
            editCarInfo.setOnClickListener { onEditClickListener.invoke(carItem) }
            itemView.setOnClickListener { onCarItemClickListener.invoke(carItem) }
            customerName.text = carItem.customerName
            carModel.text = carItem.carModel
            plateNumber.text = carItem.carPlate
            producer.text = carItem.producer
            carPhoto.setImageURI(carItem.pathImage?.toUri())
        }

    }

    override fun getItemCount(): Int = values.size

    class CarItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val customerName: TextView = itemView.findViewById(R.id.customerName)
        val carModel: TextView = itemView.findViewById(R.id.carModel)
        val plateNumber: TextView = itemView.findViewById(R.id.plateNumber)
        val producer: TextView = itemView.findViewById(R.id.carProducer)
        val editCarInfo: ImageButton = itemView.findViewById(R.id.editCarInfo)
        val carPhoto: ImageView = itemView.findViewById(R.id.carPhoto)

    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = mutableListOf<CarItem>()

                if (constraint.isNullOrEmpty()) {
                    result.addAll(listOfValuesForFilter)

                } else {
                    val enteredChars = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    listOfValuesForFilter.forEach {

                        if (it.producer.toLowerCase(Locale.getDefault()).trim().contains(enteredChars) ||
                                it.carPlate.toLowerCase(Locale.getDefault()).trim().contains(enteredChars)) {
                            result.add(it)
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = result
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                values = results?.values as MutableList<CarItem>
                notifyDataSetChanged()

            }
        }
    }
}