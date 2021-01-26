package com.homework.hw4_1_rework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class ContactListAdapter extends
        RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> implements Filterable {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private ArrayList<Contact> contactListFull = new ArrayList<>();
    private MainActivity.ListContactActionListener listContactActionListener;

    public ContactListAdapter(MainActivity.ListContactActionListener listContactActionListener) {
        this.listContactActionListener = listContactActionListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_of_recycler,
                parent, false);
        return new ContactViewHolder(view, listContactActionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void addItems(List<Contact> contacts) {
        contactListFull.clear();
        contactListFull.addAll(contacts);
        contactList.clear();
        contactList.addAll(contacts);
        notifyDataSetChanged();
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact contact : contactListFull) {
                    if (contact.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contact);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactList.clear();
            contactList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameView, infoView;
        private MainActivity.ListContactActionListener listContactActionListener;

        public ContactViewHolder(View contactView,
                                 MainActivity.ListContactActionListener listContactActionListener) {
            super(contactView);
            imageView = contactView.findViewById(R.id.contactImage);
            nameView = contactView.findViewById(R.id.contactName);
            infoView = contactView.findViewById(R.id.contactInfo);
            this.listContactActionListener = listContactActionListener;

            itemView.setOnClickListener(view -> {
                if (listContactActionListener != null) {
                    listContactActionListener.onContactClicked(getLayoutPosition());
                }
            });
        }

        public void bind(Contact contact) {
            imageView.setImageResource(contact.getImage());
            nameView.setText(contact.getName());
            infoView.setText(contact.getInfo());

        }
    }
}
