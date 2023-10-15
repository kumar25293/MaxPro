package com.sample.recyclerviewpaging.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.maxpro.systemtask.R
import com.maxpro.systemtask.model.UserDetails


class UserListAdapter(val context:Context/*,var onclick:(UserDetails.Data)->Unit*/ ) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {


    var usersList:ArrayList<UserDetails> =ArrayList<UserDetails>()


    override fun getItemCount(): Int {
        println("User list size ===${usersList?.size }")
        return usersList?.size ?: 0

    }

    fun setListData(list:List<UserDetails>,pos:Int){
        usersList.addAll(list)
        val sortedlist = usersList.sortedWith(compareBy { it.displayName })
        usersList.clear()
        usersList.addAll(sortedlist )
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userdata= usersList[position]
        holder.tvUsername.text = userdata.displayName.toString()
        val pos = position+1
        holder.tvserislno.text = pos.toString()
        holder.tvreputation.text = "(Reputation : ${userdata.reputation.toString()} )"

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.circleCrop(/*FitCenter(), RoundedCorners(16)*/)
        Glide.with(context)
            .load(userdata.profileImage)
            .apply(requestOptions)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.usrimg)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) ,View.OnClickListener{
        val tvUsername: TextView = itemView.findViewById(R.id.user_name)
        val usrimg: ImageView = itemView.findViewById(R.id.user_image)
        val tvserislno: TextView = itemView.findViewById(R.id.tv_serial_no)
        val tvreputation: TextView = itemView.findViewById(R.id.user_reputation)
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val pos= adapterPosition
//            onclick( mList?.get(pos))
        }


    }


}