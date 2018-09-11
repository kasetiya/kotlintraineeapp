package com.softices.kotlintraineeapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.android.volley.Request
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.constants.Constant
import com.softices.kotlintraineeapp.constants.UrlEndPoints
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.listeners.TaskCompleteListener
import com.softices.kotlintraineeapp.webservices.RequestJSONs
import kotlinx.android.synthetic.main.activity_post_service.*
import org.json.JSONObject

class PutServiceActivity : AppCompatActivity(), TaskCompleteListener {
    private var TAG = "PutServiceActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_service)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_post.setOnClickListener {
            if (!L.isNotNull(edt_title.text.toString())) {
                L.t(this, "please enter title.")
            } else if (!L.isNotNull(edt_body.text.toString())) {
                L.t(this, "please enter body.")
            } else {
                if (L.isNetworkConnected(this)) {
                    sendData()
                } else {
                    L.t(this, "No internet connection.")
                }
            }
        }
    }

    /**
     * create json data object and pass though put service
     */
    private fun sendData() {
        try {
            val jsObject = JSONObject()
            jsObject.put("title", edt_title.text.toString())
            jsObject.put("body", edt_body.text.toString())
            jsObject.put("userId", 1)
            RequestJSONs.sendJSONrequest(this, Request.Method.PUT, UrlEndPoints.urlListPost + "/" + 1,
                    jsObject, this, Constant.POST_REQUEST)
        } catch (e: Exception) {
            Log.e(TAG, "")
        }
    }

    /**
     * when api response is receive this listener execute.
     * @param response
     * @param serviceCode
     *
     */
    override fun onTaskCompleted(response: String, serviceCode: Int) {
        when (serviceCode) {
            Constant.POST_REQUEST -> {
                parsePostData(response)
            }
        }
    }

    /**
     * @param  response
     * parse json response data.
     * must use try catch.
     */
    private fun parsePostData(response: String) {
        if (L.isNotNull(response)) {
            L.t(this, "Data successfully updated.")
            edt_title.setText("")
            edt_body.setText("")
        } else {
            L.t(this, getString(R.string.err_something_wrong))
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}