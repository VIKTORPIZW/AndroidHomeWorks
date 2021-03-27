package by.it.academy.app8_1task

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListOfContactsFragment : Fragment(R.layout.fragment_list_of_contacts), ContactAdapter.OnContactAdapterClick {

    private lateinit var textViewNoContacts: TextView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var buttonAddContact: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            textViewNoContacts = findViewById(R.id.textNoContactsPleaseAdd)
            searchView = findViewById(R.id.searchView)
            recyclerView = findViewById(R.id.recyclerView)
            buttonAddContact = findViewById(R.id.addContactButton)
            contactAdapter = ContactAdapter(ContactListManager.getListOfContacts(), this@ListOfContactsFragment)

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = contactAdapter

            buttonAddContact.setOnClickListener {
                showAddContactFragment()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return false
            }
        })

    }

    private fun showAddContactFragment() {

        parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .replace(R.id.mainContainerFragment, AddContactFragment())
                .commit()
    }

    override fun onResume() {
        super.onResume()
        textViewNoContacts.isVisible = ContactListManager.getListOfContacts().isEmpty()
    }

    override fun onContactClick(item: Contact, position: Int) {
        val bundle = Bundle().apply {
            putInt("position", position)
            putString("name", item.name)
            putString("emailOrPhoneNumber", item.emailOrPhoneNumber)
            putInt("image", item.image)
        }

        parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .replace(R.id.mainContainerFragment, EditContactFragment::class.java, bundle)
                .commit()

    }
}