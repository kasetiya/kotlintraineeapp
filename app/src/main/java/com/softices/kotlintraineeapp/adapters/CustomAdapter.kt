package com.softices.kotlintraineeapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.models.UserModel

class CustomAdapter(val context: Context, val userList: ArrayList<UserModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_users, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        val model = userList.get(position)
        holder.tvName.text = model.firstName
        holder.tvEmail.text = model.email

        holder.ivDelete.setOnClickListener(View.OnClickListener {
            DatabaseManager(context).deleteUser(model.email.toString())
            userList.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(context, "User Deleted Successfully.", Toast.LENGTH_LONG).show()
        })
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvEmail: TextView
        var ivDelete: ImageView

        init {
            ivDelete = itemView.findViewById<ImageView>(R.id.iv_delete)
            tvName = itemView.findViewById<TextView>(R.id.tv_name)
            tvEmail = itemView.findViewById<TextView>(R.id.tv_email)
        }
    }
}