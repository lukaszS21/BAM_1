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

        textViewUsername = findViewById(R.id.textViewUsername)

        val intent = intent
        val username = intent.getStringExtra("username")
        serviceIntent = Intent(this, CountingService::class.java).apply {
            putExtra("name", extras?.getString("name"))
        }
        numberReceiver = createReceiver()

        if (username != null) {
            textViewUsername.text = "Witaj, $username!"
            Log.d("UserActivity", "Received username: $username")

            val broadcastIntent = Intent("COUNTER_DATA")
            broadcastIntent.putExtra("username", username)
            sendBroadcast(broadcastIntent)

            // Save the user to the database
            saveUser(username)
        }
    }

    @SuppressLint("CheckResult")
    fun fetchData(view: View) {
        AppDatabase.getInstance(this)?.userDao()?.getAllUsers()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { users ->
                Log.d("UserActivity", "Total Users: ${users.size}")
                for (user in users) {
                    Log.d("UserActivity", "User: ${user.username}, Number: ${user.stopperValue}")
                }
            }
    }

    @SuppressLint("CheckResult")
    fun saveUser(username: String) {
        val user = User(username, 0)
        AppDatabase.getInstance(this)?.userDao()?.insert(user)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("UserActivity", "User saved successfully.")
            }, {
                Log.e("UserActivity", "Error saving user.", it)
            })
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
