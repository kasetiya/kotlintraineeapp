package com.softices.kotlintraineeapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_chnage_password.*

class ChangePasswordActivity : AppCompatActivity() {

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chnage_password)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /***
         * chnage password button click and
         * validation on password
         */
        btn_change.setOnClickListener(View.OnClickListener {
            if (!L.isValidPassword(edt_old_password.text.toString())) {
                L.t(this, "fill password")
            } else if (!L.isValidPassword(edt_new_password.text.toString())) {
                L.t(this, "fill password")
            } else if (!L.isValidPassword(edt_new_con_password.text.toString())) {
                L.t(this, "fill password")
            } else if (!L.isPasswordMatch(edt_new_password.text.toString(), edt_new_con_password.text.toString())) {
                L.t(this, "password not match")
            } else {
                val isUpdate = DatabaseManager(this).changePassword(PreferenceHelper.getUserEmail(this)
                        , edt_old_password.text.toString(), edt_new_con_password.text.toString())
                if (isUpdate) {
                    edt_old_password.setText("")
                    edt_new_password.setText("")
                    edt_new_con_password.setText("")
                    L.t(this, "Password change successfully")
                } else {
                    L.t(this, "Please Enter valid password.")
                }
            }
        })
    }

    /**
     *
     * home menu click event
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}