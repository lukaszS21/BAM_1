package com.example.lab1

import android.annotation.SuppressLint
import android.content.Intent



import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var buttonNext: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Zadanie 1: Inicjalizacja pól widoku
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonNext = findViewById(R.id.buttonNext)

        // Zadanie 1: Ustawienie słuchacza kliknięcia na przycisku "buttonNext"
        buttonNext.setOnClickListener(View.OnClickListener {
            // Zadanie 1: Pobranie wprowadzonej nazwy użytkownika z pola tekstowego
            val username = editTextUsername.text.toString()

            // Zadanie 1: Tworzenie i konfiguracja intencji, aby przejść do UserActivity
            val intent = Intent(this@MainActivity, UserActivity::class.java)

            // Zadanie 1: Przekazanie nazwy użytkownika jako dodatkowej informacji w intencji
            intent.putExtra("username", username)

            // Zadanie 1: Rozpoczęcie aktywności UserActivity
            startActivity(intent)
        })
    }
}