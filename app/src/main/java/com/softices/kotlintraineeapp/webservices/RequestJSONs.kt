package  com.softices.kotlintraineeapp.webservices

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.listeners.TaskCompleteListener
import com.softices.kotlintraineeapp.network.VolleySingleton
import org.json.JSONObject
import java.util.*

object RequestJSONs {
    fun sendJSONrequest(context: Context, methodType: Int, url: String, jsonObject: JSONObject,
                        listner: TaskCompleteListener, serviceCode: Int) {
        L.showProgressDialog(context)
        val queue = Volley.newRequestQueue(context)
        val request = object : JsonObjectRequest(
                methodType,
                url,
                jsonObject,
                Response.Listener { response ->
                    Log.e("response", response.toString())
                    L.hideProgressDialog()
                    listner.onTaskCompleted(response.toString(), serviceCode)
                },
                Response.ErrorListener { error ->
                    L.hideProgressDialog()
                    Log.e("RequestJSONs", "ErrorListener " + error.printStackTrace());
                    Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show()
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }

        request.retryPolicy = DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

    fun sendStringRequest(context: Context, methodType: Int, url: String,
                          listner: TaskCompleteListener, serviceCode: Int) {
        L.showProgressDialog(context)
        val queue = Volley.newRequestQueue(context)
        val request = object : StringRequest(
                methodType,
                url,
                Response.Listener { response ->
                    Log.e("response", response)
                    L.hideProgressDialog()
                    listner.onTaskCompleted(response.toString(), serviceCode)
                },
                Response.ErrorListener {
                    L.hideProgressDialog()
                    Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show()
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        request.retryPolicy = DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }
}