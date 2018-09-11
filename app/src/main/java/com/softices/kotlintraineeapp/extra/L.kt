package com.softices.kotlintraineeapp.extra

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import android.provider.MediaStore
import android.util.Log


/**
 * Created by Rahul on 7/13/2018.
 */
object L {
    private var progressDialog: ProgressDialog? = null
    @SuppressLint("MissingPermission")
//---------is connected to network
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context
                .CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    fun isNotNull(setText: String?): Boolean {
        return setText != null && setText != "null" && setText != "" && !setText.isEmpty()
    }

    fun t(context: Context, setText: String?) {
        Toast.makeText(context, setText, Toast.LENGTH_LONG).show()
    }

    fun showProgressDialog(context: Context) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Loading...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
        }
    }

    // function to hide the loading dialog box
    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    //-----------------username validation
    @Throws(NumberFormatException::class)
    fun isValidName(editText: String): Boolean {
        return !(editText.isEmpty() || editText.trim { it <= ' ' } == "")
    }

    fun isValidEmail(editText: String): Boolean {
        if (editText.isEmpty()) {
            return false
        } else {
            val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$"
            val emailInput = editText.trim { it <= ' ' }
            return emailInput.matches(EMAIL_PATTERN.toRegex())
        }
    }

    @Throws(NumberFormatException::class)
    fun isValidMobile(editText: String): Boolean {
        if (editText.isEmpty() && editText.contains(" ") && editText.length < 10) {
            return false
        } else {
            val postCodePattrn = "[0-9]{4,14}"
            val postCodeInput = editText.trim { it <= ' ' }
            return postCodeInput.matches(postCodePattrn.toRegex())
        }
    }

    //-----------------password validation
    @Throws(NumberFormatException::class)
    fun isValidPassword(editText: String): Boolean {
        return if (editText.isEmpty() || editText.trim { it <= ' ' } == "") {
            false
        } else !(editText.length < 5 || editText.length > 16)
    }

    //-----------------match password and confirm password
    @Throws(NumberFormatException::class)
    fun isPasswordMatch(editPassword: String, editTextConfirmPassword: String): Boolean {
        return editPassword == editTextConfirmPassword
    }

    //-----------------moibile number validation
    @Throws(NumberFormatException::class)
    fun isValidMobile(editText: EditText): Boolean {
        if (editText.text.isEmpty() && editText.text.toString().contains(" ")) {
            return false
        } else {
            val postCodePattrn = "[0-9]{4,14}"
            val postCodeInput = editText.text.toString().trim { it <= ' ' }
            return postCodeInput.matches(postCodePattrn.toRegex())
        }
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } catch (e: Exception) {
            Log.e("getRealPathFromUri ", " $e")
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
        return ""
    }
}