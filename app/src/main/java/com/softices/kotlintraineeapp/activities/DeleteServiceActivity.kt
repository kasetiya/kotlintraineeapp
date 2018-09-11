package com.softices.kotlintraineeapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.android.volley.Request
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.constants.Constant
import com.softices.kotlintraineeapp.constants.UrlEndPoints
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.listeners.TaskCompleteListener
import com.softices.kotlintraineeapp.webservices.RequestJSONs
import kotlinx.android.synthetic.main.activity_delete_service.*
import org.json.JSONObject

class DeleteServiceActivity : AppCompatActivity(), TaskCompleteListener {
    private var TAG = "DeleteServiceActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_service)
        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_delete_post.setOnClickListener {
            sendData()
        }
    }

    /**
     * create json data object and pass though post service
     */
    private fun sendData() {
        RequestJSONs.sendStringRequest(this, Request.Method.DELETE, UrlEndPoints.urlListPost + "/" + 1,
                this, Constant.POST_REQUEST)
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
            L.t(this, "Data successfully deleted.")
        } else {
            L.t(this, getString(R.string.err_something_wrong))
        }
    }

    /**
     * menu home arrow click
     * on back press event
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
