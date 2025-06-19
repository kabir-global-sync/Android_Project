package com.example.carbookingapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileRecyclerViewAdapter(private val list: List<Option>) :
    RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val optionNameText: TextView = view.findViewById(R.id.textProfileNameid)
        val optionImage: ImageView = view.findViewById(R.id.imageProfilePictureid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.optionNameText.text = currentItem.name
        holder.optionImage.setImageResource(currentItem.res)

        holder.itemView.setOnClickListener {
            Log.d("ProfileAdapter", "Clicked on: ${currentItem.name}")
            // Add your click handling here
        }
    }

    override fun getItemCount(): Int = list.size
}