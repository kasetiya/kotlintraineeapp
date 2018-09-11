package com.softices.kotlintraineeapp.receiver

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast


class PlugInControlReceiver : BroadcastReceiver() {
    override
    fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == Intent.ACTION_POWER_CONNECTED) {
            Toast.makeText(context, "Power Connected", Toast.LENGTH_SHORT).show();
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}