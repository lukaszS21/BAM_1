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

// Usługa odpowiedzialna za liczenie i wysyłanie wyników poprzez nadawanie
class CountingService : Service() {
    private val unsubscribe = PublishSubject.create<Any>()
    private var lastValue: Long = 0  // Zmienna przechowująca ostatnią wartość

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val broadcastName = Intent()
            broadcastName.action = "COUNTER_DATA"
            broadcastName.putExtra("name", it.getStringExtra("name"))
            sendBroadcast(broadcastName)

            // Observable emitujący wartości co sekundę
            val observable = Observable.interval(0, 1, TimeUnit.SECONDS)

            val observer = object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("onSubscribe", "Rozpoczęto usługę")
                }

                override fun onNext(value: Long) {
                // Log.d("CountingService", value.toString())

                    // Aktualizacja lastValue nową wartością
                    lastValue = value

                    // Wysyłanie wyniku liczenia poprzez nadawanie
                    val broadcast = Intent()
                    broadcast.action = "COUNTER_DATA"
                    broadcast.putExtra("name", it.getStringExtra("name"))
                    broadcast.putExtra("number", value.toInt())
                    sendBroadcast(broadcast)
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {
                    // Wysyłanie ostatniej wartości po zakończeniu liczenia
                    val broadcast = Intent()
                    broadcast.action = "COUNTER_DATA"
//                    broadcast.putExtra("name", it.getStringExtra("name"))
//                    broadcast.putExtra("number", lastValue.toInt())
                    sendBroadcast(broadcast)
                }
            }

            // Subskrypcja z observable do observera z możliwością zatrzymania
            observable.takeUntil(unsubscribe).subscribe(observer)
        }

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // Przy zniszczeniu usługi, zatrzymuje obserwowanie
    override fun onDestroy() {
        unsubscribe.onNext(0)
    }
}
