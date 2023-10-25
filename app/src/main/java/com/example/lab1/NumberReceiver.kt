package com.example.lab1

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lab1.database.AppDatabase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

// Odbiornik służący do nasłuchiwania wiadomości broadcast z liczbami
class NumberReceiver : BroadcastReceiver() {
    @SuppressLint("CheckResult")
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.extras?.run {
            // Pobiera dane z przesłanej wiadomości
            val name = getString("name")
            val number = if (containsKey("number")) getInt("number") else null

            // Rejestruje odebrane dane w dzienniku logów
            name?.let { Log.d("Number Receiver", "Odebrano nazwę: $it") }
            number?.let { Log.d("Number Receiver", "Odebrano numer: $it") }

            // Jeśli odebrano zarówno nazwę, jak i numer, dodaje użytkownika do bazy danych
            if (name != null && number != null) {
                val user = User(username = name, stopperValue = number)
                Completable.fromAction {
                    AppDatabase.getInstance(context!!)?.userDao()?.insert(user)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // Informuje o pomyślnym dodaniu użytkownika do bazy danych
                        Log.d("Number Receiver", "Pomyślnie dodano użytkownika do bazy danych")
                    }, { error ->
                        // Informuje o błędzie podczas dodawania użytkownika do bazy danych
                        Log.e("Number Receiver", "Błąd podczas dodawania użytkownika do bazy danych: ", error)
                    })
            }
        }
    }
}

