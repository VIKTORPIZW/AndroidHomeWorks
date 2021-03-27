package by.it.academy.app8_1task


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale
import kotlin.collections.ArrayList


class ContactAdapter(private val values: List<Contact>, private val onContactAdapterClick: OnContactAdapterClick) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    var contactFilterList: List<Contact> = values

    interface OnContactAdapterClick {
        fun onContactClick(item: Contact, position: Int)
    }

    override fun getItemCount(): Int = contactFilterList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_block_contact, parent, false)

        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact = contactFilterList[position]
        with(holder) {
            textViewName.text = contact.name
            textViewEmailOrPhone.text = contact.emailOrPhoneNumber
            imageView.setImageResource(contact.image)

            itemView.setOnLongClickListener {
                onContactAdapterClick.onContactClick(contact, position)
                true
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView = itemView.findViewById(R.id.textViewName)
        var textViewEmailOrPhone: TextView = itemView.findViewById(R.id.textViewPhoneNumberOrEmail)
        var imageView: ImageView = itemView.findViewById(R.id.imageButtonContact)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isBlank()) {
                    contactFilterList = values
                } else {
                    val resultList = mutableListOf<Contact>()
                    for (row in values) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    contactFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = contactFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactFilterList = results?.values as ArrayList<Contact>
                notifyDataSetChanged()
            }

        }
    }


}