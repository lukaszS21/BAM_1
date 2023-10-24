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
    private val unsubscribe = PublishSubject.create<Any>()
    private var lastValue: Long = 0  // Dodaj zmienną, aby przechowywać ostatnią wartość

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val broadcastName = Intent()
            broadcastName.action = "COUNTER_DATA"
            broadcastName.putExtra("name", it.getStringExtra("name"))
            sendBroadcast(broadcastName)

            val observable = Observable.interval(0, 1, TimeUnit.SECONDS)

            val observer = object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("onSubscribe", "Usługa uruchomiona")
                }

                override fun onNext(value: Long) {
                    Log.d("CountingService", value.toString())

                    // Aktualizuj lastValue za każdym razem, gdy dostaniesz nową wartość
                    lastValue = value

                    val broadcast = Intent()
                    broadcast.action = "COUNTER_DATA"
                    broadcast.putExtra("name", it.getStringExtra("name"))
                    broadcast.putExtra("number", value.toInt())
                    sendBroadcast(broadcast)
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {
                    val broadcast = Intent()
                    broadcast.action = "COUNTER_DATA"
                    broadcast.putExtra("name", it.getStringExtra("name"))

                    // Wykorzystaj ostatnią znaną wartość lastValue w onComplete
                    broadcast.putExtra("number", lastValue.toInt())
                    sendBroadcast(broadcast)
                }
            }

            observable.takeUntil(unsubscribe).subscribe(observer)
        }

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        unsubscribe.onNext(0)
    }
}


