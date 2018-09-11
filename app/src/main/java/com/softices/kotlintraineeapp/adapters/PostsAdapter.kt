package com.softices.kotlintraineeapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.models.Posts

open class PostsAdapter(val data: ArrayList<Posts>, context: Context)  : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_posts, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PostsAdapter.ViewHolder, position: Int) {
        val model = data.get(position)
        holder.tvTitle.text = model.title
        holder.tvBody.text = model.body
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvBody: TextView

        init {
            tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
            tvBody = itemView.findViewById<TextView>(R.id.tv_body)
        }
    }
}