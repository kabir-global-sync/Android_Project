package com.example.carbookingapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileRecyclerViewAdapter(private val list:List<Option>): RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val optionNameText = view.findViewById<TextView>(R.id.textProfileNameid)
        val optionImage = view.findViewById<ImageView>(R.id.imageProfilePictureid)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProfileRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        holder.optionNameText.text = list[position].name
        holder.optionImage.setImageResource(list[position].res)
        holder.itemView.setOnClickListener {
            Log.d("ProfileRecyclerViewAdapter","${list[position].name} is click")
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }


}