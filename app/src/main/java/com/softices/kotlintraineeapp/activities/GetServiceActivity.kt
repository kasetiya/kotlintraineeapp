package com.softices.kotlintraineeapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.android.volley.Request
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.adapters.PostsAdapter
import com.softices.kotlintraineeapp.constants.Constant
import com.softices.kotlintraineeapp.constants.Jk
import com.softices.kotlintraineeapp.constants.UrlEndPoints
import com.softices.kotlintraineeapp.listeners.TaskCompleteListener
import com.softices.kotlintraineeapp.models.Posts
import com.softices.kotlintraineeapp.webservices.RequestJSONs
import org.json.JSONArray

class GetServiceActivity : AppCompatActivity(), TaskCompleteListener {

    private var TAG = "GetServiceActivity"
    private lateinit var recyclerViewPosts: RecyclerView
    private var arrayList: ArrayList<Posts> = ArrayList()
    private lateinit var listener: TaskCompleteListener
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var postAdapter: PostsAdapter
    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_service)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        listener = this
        recyclerViewPosts = findViewById(R.id.recycler_view_posts)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewPosts.layoutManager = linearLayoutManager
        recyclerViewPosts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        postAdapter = PostsAdapter(arrayList, this)
        recyclerViewPosts.adapter = postAdapter
    }

    /**
     * activity on resume
     * call api service.
     */
    override fun onResume() {
        super.onResume()
        RequestJSONs.sendStringRequest(this, Request.Method.GET, UrlEndPoints.urlListPost, listener, Constant.GET_REQUEST)
    }

    /**
     * when api response is receive this listener execute.
     * @param response
     * @param serviceCode
     *
     */
    override fun onTaskCompleted(response: String, serviceCode: Int) {
        when (serviceCode) {
            Constant.GET_REQUEST -> {
                parseGetRequest(response)
            }
        }
    }

    /**
     * @param  response
     * parse json response data.
     * must use try catch.
     */
    private fun parseGetRequest(response: String) {
        try {
            val jsResponse = JSONArray(response)
            if (jsResponse.length() > 0) {
                for (i in 0..(jsResponse.length() - 1)) {
                    val jsonObject = jsResponse.getJSONObject(i)
                    val id = jsonObject.getInt(Jk.JK_ID)
                    val userId = jsonObject.getInt(Jk.JK_USER_ID)
                    val title = jsonObject.getString(Jk.JK_TITLE)
                    val body = jsonObject.getString(Jk.JK_BODY)
                    arrayList.add(Posts(title, body, id, userId))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "parseGetRequest $e");
        } finally {
            changeUI()
        }
    }

    // change ui after data parse
    private fun changeUI() {
        if (arrayList.size > 0) {
            postAdapter.notifyDataSetChanged()
        }
    }

    //  menu arrow click
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}