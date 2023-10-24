package com.example.lab1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var buttonNext: Button

    private val PERMISSION_REQUEST_CODE = 101

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Zadanie 1: Inicjalizacja pól widoku
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonNext = findViewById(R.id.buttonNext)

        // Prośba o uprawnienia
        requestPermission()

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

    private fun requestPermission() {
        val customPermission = "com.example.lab1.permission.READ_USER_DATA"
        if (ContextCompat.checkSelfPermission(this, customPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(customPermission), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienie zostało przyznane
            } else {
                // Uprawnienie zostało odrzucone
            }
        }
    }
}
