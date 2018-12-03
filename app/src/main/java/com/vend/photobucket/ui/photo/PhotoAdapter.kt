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
import java.util.*

class PhotoAdapter(private var data: ArrayList<Image>,
                   private val photoAdapterListener: PhotoAdapterListener,
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
            photoAdapterListener.showImageDetails(data[viewHolder.adapterPosition])
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
            photoAdapterListener.deleteImages(list)
            notifyDataSetChanged()
            return true
        } else {
            if (selection.isEmpty()) {
                return false
            }

            data.removeAll(selection)
            photoAdapterListener.deleteImages(selection)
            selection.clear()
            notifyDataSetChanged()

            return true
        }
    }

    fun shuffle(){
        data.shuffle()

        notifyDataSetChanged()
    }

    fun sortByTitle(){
        data.sortWith(Comparator { p0, p1 -> p0!!.title.compareTo(p1.title)})
        notifyDataSetChanged()
    }

    fun sortByDate(ascending: Boolean){
        val order = if(ascending) 1 else -1
        data.sortWith(kotlin.Comparator { p0, p1 ->  (p0!!.saveTimeInMillis.compareTo(p1.saveTimeInMillis) * order)})
        notifyDataSetChanged()
    }
}