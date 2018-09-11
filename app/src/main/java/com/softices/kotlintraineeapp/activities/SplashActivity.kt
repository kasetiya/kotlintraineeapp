package com.softices.kotlintraineeapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric


class SplashActivity : AppCompatActivity() {

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //initialize fireabase instance
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result.token
                    Log.e("TAG", token)
                })
        FirebaseMessaging.getInstance().subscribeToTopic("video");
        Fabric.with(this, Crashlytics())
        callSigninActivity()
    }

    /**
     * thread sleeps for 2s and then
     * if user is login than redirect to dashboard otherwise to sign in activity.
     */
    private fun callSigninActivity() {
        val timerThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent: Intent
                    if (PreferenceHelper.getIsLogin(this@SplashActivity)) {
                        intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                    } else
                        intent = Intent(this@SplashActivity, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        timerThread.start()
    }
}