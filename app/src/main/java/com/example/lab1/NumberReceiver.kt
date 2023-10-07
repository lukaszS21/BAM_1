package com.example.lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class NumberReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras
        val name = extras?.getString("name")
        val number = extras?.getInt("number")

        name?.let {
            Log.d("Number Receiver", "Received name: $it")
        }

        number?.let {
            Log.d("Number Receiver", "Received number: $it")
        }

    }
}
