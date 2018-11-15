package com.vend.photobucket.ui.photo.details

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.vend.photobucket.R


class SelectContactsAdapter(private var data: ArrayList<String>,
                            private val contactsAdapterListener: ContactsAdapterListener)
    : RecyclerView.Adapter<SelectContactsAdapter.ViewHolder>() {

    private val selection: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_contact, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contact = data[position]

        holder.contactInfo.text = contact

        setListeners(holder, contact)

        if (selection.isEmpty())
            holder.checkbox.visibility = View.GONE
        else
            holder.checkbox.visibility = View.VISIBLE

        if (selection.contains(contact)) {
            holder.parent.setBackgroundColor(Color.CYAN)
            holder.checkbox.isChecked = true
        } else {
            holder.parent.setBackgroundColor(Color.LTGRAY)
            holder.checkbox.isChecked = false
        }
    }

    class ViewHolder(contactView: View) : RecyclerView.ViewHolder(contactView) {
        val parent: View = contactView.findViewById(R.id.parent)
        val checkbox: CheckBox = contactView.findViewById(R.id.checkbox)
        val contactInfo: TextView = contactView.findViewById(R.id.tvContactInfo)
    }

    fun selectAll() {
        selection.addAll(data)
        notifyDataSetChanged()
    }

    fun send(): Boolean {
        if (selection.isEmpty())
            return false
        else
            sendImage()
        return true
    }

    private fun sendImage() {
        contactsAdapterListener.sendImage(selection)
    }

    private fun setListeners(holder: ViewHolder, contact: String) {
        holder.checkbox.setOnClickListener {
            if (selection.contains(contact)) {
                selection.remove(contact)
            } else {
                selection.add(contact)
            }

            notifyDataSetChanged()
        }

        holder.parent.setOnLongClickListener {
            selection.add(contact)
            holder.checkbox.visibility = View.VISIBLE
            notifyDataSetChanged()
            true
        }

    }
}