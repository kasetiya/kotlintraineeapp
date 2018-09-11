package com.softices.kotlintraineeapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.adapters.CustomAdapter
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.models.UserModel


class UsersActivity : AppCompatActivity() {
    private lateinit var recyclerViewUsers: RecyclerView
    private var arrayList: ArrayList<UserModel> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerViewUsers = findViewById(R.id.recycler_view_users)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewUsers.layoutManager = linearLayoutManager
        recyclerViewUsers.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        arrayList.addAll(DatabaseManager(applicationContext).getAllUserData(this))

        val cad = CustomAdapter(this, arrayList)
        recyclerViewUsers.adapter = cad
    }

    /**
     *menu back arrow pressed
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}