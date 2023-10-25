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

        // Inicjalizuje komponenty interfejsu użytkownika
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonNext = findViewById(R.id.buttonNext)

        // Wysyła prośbę o przyznanie uprawnień
        requestPermission()

        // Ustawia reakcję na kliknięcie przycisku "buttonNext"
        buttonNext.setOnClickListener(View.OnClickListener {
            // Pobiera wprowadzoną nazwę użytkownika
            val username = editTextUsername.text.toString()

            // Tworzy intencję przeniesienia do UserActivity
            val intent = Intent(this@MainActivity, UserActivity::class.java)

            // Dołącza nazwę użytkownika do intencji
            intent.putExtra("username", username)

            // Uruchamia UserActivity
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
                // Obsługuje sytuację, gdy uprawnienie zostało przyznane
            } else {
                // Obsługuje sytuację, gdy uprawnienie zostało odrzucone
            }
        }
    }
}
