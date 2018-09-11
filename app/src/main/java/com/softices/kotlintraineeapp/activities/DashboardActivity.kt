package com.softices.kotlintraineeapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    /**
     * @return Boolean
     * @param item
     * Handle navigation view item clicks here.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.nav_user -> {
                startActivity(Intent(this, UsersActivity::class.java))
            }
            R.id.nav_contacts -> {
                startActivity(Intent(this, ContactsActivity::class.java))
            }
            R.id.nav_map -> {
                startActivity(Intent(this, GoogleMapActivity::class.java))
            }
            R.id.nav_service -> {
                startActivity(Intent(this, WebserviceActivity::class.java))
            }
            R.id.nav_broadcast -> {
                startActivity(Intent(this, BroadcastActivity::class.java))
            }
            R.id.nav_dialog -> {
                startActivity(Intent(this, DialogActivity::class.java))
            }
            R.id.nav_back_service -> {
                startActivity(Intent(this, BackgroundServiceActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    /**
     * menu items click event
     * @param item
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_logout ->
                showLogoutDialog()
            R.id.action_change_password ->
                startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        return true
    }

    /**
     * alert dailog for logout
     */
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.txt_logout))
                .setMessage(getString(R.string.msg_logout))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.txt_yes),
                        DialogInterface.OnClickListener { dialog, which ->
                            PreferenceHelper.clearPref(this)
                            startActivity(Intent(this, SigninActivity::class.java))
                            finishAffinity()
                        })
                .setNegativeButton(getString(R.string.txt_no),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }).show()
    }

    /**
     * on back press event
     */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}