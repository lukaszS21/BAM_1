package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private lateinit var textViewUsername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Zadanie 1: Inicjalizacja pola TextView do wyświetlania nazwy użytkownika
        textViewUsername = findViewById(R.id.textViewUsername)

        // Zadanie 1: Pobranie przekazanej nazwy użytkownika z intencji
        val intent = intent
        val username = intent.getStringExtra("username")

        // Zadanie 1: Wyświetlenie nazwy użytkownika w elemencie TextView
        if (username != null) {
            textViewUsername.text = "Witaj, $username!"
        }
    }
}
