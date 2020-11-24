package com.example.galacticon

import android.content.Intent
import android.provider.ContactsContract
import android.util.    Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*



class RecyclerAdapter(var  listOfString: List<String>) :RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_row,parent,false)
        return PhotoHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        val itemPhoto = listOfString[position]
        holder.name.text = itemPhoto

    }

    override fun getItemCount(): Int {
        return listOfString.size
    }

    class PhotoHolder(v:View):RecyclerView.ViewHolder(v) {


        var name:TextView = v.findViewById(R.id.itemDescription)

    }

}



