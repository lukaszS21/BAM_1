package com.example.lab1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab1.database.AppDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

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
    @SuppressLint("CheckResult")
    fun fetchData(view: View) {
        AppDatabase.getInstance(this)?.userDao()?.getAllUsers()
            ?.subscribeOn(Schedulers.io())  // Run on a background thread
            ?.observeOn(AndroidSchedulers.mainThread())  // Observe on the main thread
            ?.subscribe { users ->
                Log.d("UserActivity", "Total Users: ${users.size}")
                for (user in users) {
                    Log.d("UserActivity", "User: ${user.username}, Number: ${user.stopperValue}")
                }
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