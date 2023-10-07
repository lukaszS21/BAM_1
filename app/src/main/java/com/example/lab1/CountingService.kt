package com.example.lab1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CountingService : Service() {
    // Tworzymy obiekt do obsługi zakończenia subskrypcji.
    private val unsubscribe = PublishSubject.create<Any>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Sprawdzamy, czy dostaliśmy Intencję.
        intent?.let {
            // Tworzymy nową Intencję, która zostanie wysłana jako broadcast.
            val broadcastName = Intent()
            broadcastName.action = "COUNTER_DATA"
            // Dodajemy dodatkowe informacje do Intencji.
            broadcastName.putExtra("name", it.getStringExtra("name"))
            // Wysyłamy broadcast z informacją o nazwie.

            sendBroadcast(broadcastName)

            // Tworzymy obiekt Observable, który generuje liczby co sekundę.
            val observable = Observable.interval(0, 1, TimeUnit.SECONDS)
            var lastEmitted: Int? = null

            // Tworzymy obiekt obserwatora, który nasłuchuje na zmiany w Observable.
            val observer = object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    // Obsługa rozpoczęcia subskrypcji.
                    Log.d("onSubscribe", "Usługa uruchomiona")
                }

                override fun onNext(value: Long) {
                    // Obsługa nowej wartości z Observable.
                    Log.d("Counting service", value.toString())
                    lastEmitted = value.toInt()
                }

                override fun onError(e: Throwable) {
                    // Obsługa błędu (nie używamy w tym przypadku).
                }

                override fun onComplete() {
                    // Obsługa zakończenia strumienia danych.
                    val broadcast = Intent()
                    broadcast.action = "COUNTER_DATA"
                    // Dodajemy nazwę do Intencji.
                    broadcast.putExtra("name", it.getStringExtra("name"))
                    lastEmitted?.let { number ->
                        // Dodajemy numer do Intencji, jeśli jest dostępny.
                        broadcast.putExtra("number", number)
                        // Wysyłamy broadcast z informacjami.
                        sendBroadcast(broadcast)
                    }
                }
            }

            // Subskrybujemy obserwatora do Observable i ustawiamy limit subskrypcji.
            observable.takeUntil(unsubscribe).subscribe(observer)
        }

        // Zwracamy flagę informującą, że serwis jest uruchamiany ponownie w przypadku zamknięcia.
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onDestroy() {
        // Wywołujemy zakończenie subskrypcji, gdy serwis zostanie zniszczony.
        unsubscribe.onNext(0)
    }
}
