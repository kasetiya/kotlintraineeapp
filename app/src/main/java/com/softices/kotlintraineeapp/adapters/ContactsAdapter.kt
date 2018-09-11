package com.softices.kotlintraineeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.models.ContactModel

open class ContactsAdapter(data: ArrayList<ContactModel>, context: Context) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)
    var localData = data
    var tempData: ArrayList<ContactModel> = ArrayList()

    init {
        tempData.addAll(localData)
    }

    override fun getCount(): Int {
        return localData.size
    }

    override fun getItem(position: Int): Any {
        return localData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun filter(charText: String) {
        val charText = charText.toLowerCase()
        localData.clear()
        if (charText.length == 0) {
            localData.addAll(tempData)
        } else {
            for (wp in tempData) {
                val text = wp.contactName.toLowerCase()
                if (text.contains(charText)) {
                    localData.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.layout_row_contacts, parent, false)
            vh = ListRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        vh.tvName.text = localData.get(index = position).contactName
        vh.tvNumber.text = localData.get(index = position).contactNumber
        return view
    }

    private class ListRowHolder(row: View?) {
        val tvName: TextView = row!!.findViewById<TextView>(R.id.tv_name) as TextView
        val tvNumber: TextView = row!!.findViewById<TextView>(R.id.tv_number) as TextView
    }
}