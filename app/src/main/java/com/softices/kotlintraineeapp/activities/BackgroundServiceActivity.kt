package com.softices.kotlintraineeapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.service.MyService
import kotlinx.android.synthetic.main.activity_background_service.*

class BackgroundServiceActivity : AppCompatActivity() {

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_service)

        init()
    }

    /***
     * initialize activity.
     */
    fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val i = Intent(applicationContext, MyService::class.java)
        //click event
        btn_start.setOnClickListener {
            startService(i)
        }
        btn_stop.setOnClickListener {
            stopService(i)
        }
    }
    /**
     * back arrow pressed
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}