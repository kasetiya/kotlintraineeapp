package com.softices.kotlintraineeapp.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.FirebaseApp
import com.softices.kotlintraineeapp.database.DatabaseManager

class MyApplication : Application() {
    val TAG = "KotlinTraineeApp"
    lateinit var instance: MyApplication
    lateinit var appPAckageName: String

    override fun onCreate() {
        super.onCreate()
        appPAckageName = packageName
        instance = this
        MyApplication.context = applicationContext
        FirebaseApp.initializeApp(this)
//        mDatabase = DatabaseManager(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmField
        var context: Context? = null

        // Not really needed since we can access the variable directly.
        @JvmStatic
        fun getMyApplicationContext(): Context? {
            return context
        }
    }

    //hide keyboard
    fun hideKeyboard(view: View?, context: Context) {
        // Check if no view has focus:
        if (view != null) {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}