package by.it.academy.app8_1task

    object ContactListManager {
        private val listOfContacts = mutableListOf<Contact>()

        fun addContactToList(contact: Contact) {
            listOfContacts.add(contact)
        }

        fun editContactInList(index: Int, contact: Contact) {
            listOfContacts[index] = contact
        }

        fun deleteContactFromList(index: Int) {
            listOfContacts.removeAt(index)
        }

        fun getListOfContacts(): List<Contact> {
            return listOfContacts
        }

    }
