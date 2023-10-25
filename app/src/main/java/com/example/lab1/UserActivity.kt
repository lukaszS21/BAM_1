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

        // Inicjalizacja widoku z nazwą użytkownika
        textViewUsername = findViewById(R.id.textViewUsername)

        val intent = intent
        val username = intent.getStringExtra("username")

        // Utworzenie intencji dla usługi CountingService
        serviceIntent = Intent(this, CountingService::class.java).apply {
            putExtra("name", extras?.getString("name"))
        }
        numberReceiver = createReceiver()

        if (username != null) {
            textViewUsername.text = "Witaj, $username!"
            Log.d("UserActivity", "Received username: $username")

            // Wysłanie informacji o nazwie użytkownika do odbiorcy
            val broadcastIntent = Intent("COUNTER_DATA")
            broadcastIntent.putExtra("username", username)
            sendBroadcast(broadcastIntent)

            // Zapisanie użytkownika w bazie danych
            saveUser(username)
        }
    }

    @SuppressLint("CheckResult")
    fun fetchData(view: View) {
        // Pobranie danych użytkowników z bazy danych
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
        // Zapisanie nowego użytkownika w bazie danych
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
        // Rozpoczęcie usługi CountingService
        startService(serviceIntent)
    }

    fun stopService(view: View) {
        // Zatrzymanie usługi CountingService
        stopService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Wyrejestrowanie odbiorcy przy zakończeniu działania aktywności
        unregisterReceiver(numberReceiver)
    }

    private fun createReceiver(): NumberReceiver {
        // Utworzenie i rejestracja odbiorcy dla danych licznika
        val filter = IntentFilter("COUNTER_DATA")
        val receiver = NumberReceiver()
        registerReceiver(receiver, filter)
        return receiver
    }
}
