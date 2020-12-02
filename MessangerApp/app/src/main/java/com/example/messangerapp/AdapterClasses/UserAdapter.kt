package com.example.messangerapp.AdapterClasses

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messangerapp.ModelClasses.Users
import com.example.messangerapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val mContext: Context?, val mUsers: List<Users>, val isChatCheck:Boolean): RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout,parent,false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        Log.d("tag ", "on bind")
        holder.usernameTxt.text = user.getUsername()
        Picasso.get().load(user.getPofile()).into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        Log.d("tag " , "size : "+mUsers.size)
         return mUsers.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
      /*  lateinit var usernameTxt: TextView
        lateinit var profileImageView: CircleImageView
        lateinit var onlineImageView: CircleImageView
        lateinit var offlineImageView: CircleImageView
        lateinit var lastMessageTxt: TextView
*/

            var usernameTxt = itemView.findViewById<TextView>(R.id.username)
            var profileImageView = itemView.findViewById<CircleImageView>(R.id.profile_image)
            var onlineImageView = itemView.findViewById<CircleImageView>(R.id.image_online)
            var offlineImageView = itemView.findViewById<CircleImageView>(R.id.image_offline)
            var lastMessageTxt = itemView.findViewById<TextView>(R.id.message_last)

    }
}