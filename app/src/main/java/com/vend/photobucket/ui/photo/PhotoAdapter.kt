package com.vend.photobucket.ui.photo

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vend.photobucket.R
import com.vend.photobucket.model.Image

class PhotoAdapter(private var data: ArrayList<Image>,
                   private val adapterListener: AdapterListener,
                   private val context: Context) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

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

        val viewHolder = ViewHolder(view)

        viewHolder.parent.setOnClickListener {
            adapterListener.showImageDetails(data[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img: Image = data[position]

        holder.tvTitle.text = img.title

        val picasso = Picasso.Builder(context).build()
        picasso.load("file://${img.path}")
                .placeholder(R.drawable.twotone_add_a_photo_24)
                .into(holder.imageView)

        holder.checkbox.setOnClickListener {
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


        if (selection.isEmpty())
            holder.checkbox.visibility = View.GONE
        else
            holder.checkbox.visibility = View.VISIBLE

        if (selection.contains(img)) {
            holder.parent.setBackgroundColor(Color.CYAN)
            holder.checkbox.isChecked = true
        } else {
            holder.parent.setBackgroundColor(Color.LTGRAY)
            holder.checkbox.isChecked = false
        }
    }

    fun setData(data: ArrayList<Image>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun selectAll() {
        selection.addAll(data)
        notifyDataSetChanged()
    }

    fun delete(list: ArrayList<Image>? = null): Boolean {
        if (list != null) {
            data.removeAll(list)
            adapterListener.deleteImages(list)
            notifyDataSetChanged()
            return true
        } else {
            if (selection.isEmpty()) {
                return false
            }

            data.removeAll(selection)
            adapterListener.deleteImages(selection)
            selection.clear()
            notifyDataSetChanged()

            return true
        }
    }
}