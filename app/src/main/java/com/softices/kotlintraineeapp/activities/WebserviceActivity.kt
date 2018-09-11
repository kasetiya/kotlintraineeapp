package com.softices.kotlintraineeapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.softices.kotlintraineeapp.R
import kotlinx.android.synthetic.main.activity_webservice.*

class WebserviceActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservice)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_get.setOnClickListener(this)
        btn_post.setOnClickListener(this)
        btn_put.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
    }

    /***
     * @param p0
     * view onclick event
     */
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_get -> {
                startActivity(Intent(this, GetServiceActivity::class.java))
            }
            R.id.btn_post -> {
                startActivity(Intent(this, PostServiceActivity::class.java))
            }
            R.id.btn_put -> {
                startActivity(Intent(this, PutServiceActivity::class.java))
            }
            R.id.btn_delete -> {
                startActivity(Intent(this, DeleteServiceActivity::class.java))
            }
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