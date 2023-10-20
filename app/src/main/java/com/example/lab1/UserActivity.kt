package com.example.lab1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class UserActivity : AppCompatActivity() {
    private lateinit var serviceIntent: Intent
    private lateinit var numberReceiver: NumberReceiver

    private lateinit var textViewUsername: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Zadanie 1: Inicjalizacja pola TextView do wyświetlania nazwy użytkownika
        textViewUsername = findViewById(R.id.textViewUsername)

        // Zadanie 1: Pobranie przekazanej nazwy użytkownika z intencji
        val intent = intent
        val username = intent.getStringExtra("username")
        serviceIntent = Intent(this, CountingService::class.java).apply {
            putExtra("name", extras?.getString("name"))
        }
        numberReceiver = createReceiver()

        // Zadanie 1: Wyświetlenie nazwy użytkownika w elemencie TextView
        if (username != null) {
            textViewUsername.text = "Witaj, $username!"
            // Send a broadcast with the username
            // Log the username directly in UserActivity
            Log.d("UserActivity", "Received username: $username")

            // Send a broadcast with the username
            val broadcastIntent = Intent("COUNTER_DATA")
            broadcastIntent.putExtra("username", username)
            sendBroadcast(broadcastIntent)
        }
    }

    fun startService(view: View) {
        startService(serviceIntent)
    }

    fun stopService(view: View) {
        stopService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(numberReceiver)
    }

    private fun createReceiver(): NumberReceiver {
        val filter = IntentFilter("COUNTER_DATA")
        val receiver = NumberReceiver()
        registerReceiver(receiver, filter)
        return receiver
    }
}