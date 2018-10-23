package com.vend.photobucket.ui.photo

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.vend.photobucket.R
import com.vend.photobucket.R.id.btnSelectAll
import com.vend.photobucket.model.Image

class PhotoAdapter(private val data: List<Image>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private val selection: ArrayList<Image> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent: View = itemView.findViewById(R.id.parent)
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = data[position]

        holder.tvTitle.text = img.title
        //todo: add image

        holder.checkbox.setOnClickListener { //setOnCheckedChangeListener { _, _ ->
            if (selection.contains(img)) {
                selection.remove(img)
            } else {
                selection.add(img)
            }

            notifyDataSetChanged()
        }

        holder.parent.setOnLongClickListener {
            selection.add(img)
            holder.checkbox.visibility = View.VISIBLE
            notifyDataSetChanged()
            true
        }


        if (selection.isEmpty()){
            holder.checkbox.visibility = View.GONE

        }

        if (selection.contains(img)) {
            holder.parent.setBackgroundColor(Color.CYAN)
            holder.checkbox.isChecked = true
        } else{
            holder.parent.setBackgroundColor(Color.LTGRAY)
            holder.checkbox.isChecked = false
        }
    }

    fun selectAll(){
        data.forEach{
            selection.add(it)
        }
        notifyDataSetChanged()
    }
}