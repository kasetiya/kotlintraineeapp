package com.softices.kotlintraineeapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_signin.*


class SigninActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        init();
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)

        //login button initialize and clicked
        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener(View.OnClickListener {
            if (!L.isNotNull(edtEmail.text.toString())) {
                L.t(this, "please enter email")
            } else if (!L.isValidPassword(edtPassword.text.toString())) {
                L.t(this, "password must be 5 character long.")
            } else {
                //do database code
                val userModel = DatabaseManager(applicationContext).checkUserLogin(edtEmail.text.toString(),
                        edtPassword.text.toString())
                if (L.isNotNull(userModel.email)) {
                    PreferenceHelper.setIsLogin(this, true)
                    PreferenceHelper.setUserEmail(this, edtEmail.text.toString())
                    startActivity(Intent(this, DashboardActivity::class.java))
                } else {
                    L.t(this, "email or password does not match.")
                }
            }

        })

        tv_link_signup.setOnClickListener {
            val i = Intent(this, SignupActivity::class.java);
            startActivity(i)
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