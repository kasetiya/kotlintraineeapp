package com.softices.kotlintraineeapp.activities

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.receiver.PlugInControlReceiver

class BroadcastActivity : AppCompatActivity() {
    lateinit var pad: PlugInControlReceiver

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        configureReceiver()
    }

    /***
     * create intent filters and register receiver
     */
    private fun configureReceiver() {
        pad = PlugInControlReceiver()
        val powerConnectedFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        val powerdisConnectedFilter = IntentFilter(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(pad, powerConnectedFilter)
        registerReceiver(pad, powerdisConnectedFilter)
    }

    /***
     * unregister broadcast receiver.
     */
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pad)
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