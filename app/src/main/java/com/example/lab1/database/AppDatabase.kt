package com.example.lab1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab1.User

// Definicja głównej bazy danych aplikacji z encją User
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Dostęp do funkcji bazodanowych dla encji User
    abstract fun userDao(): UserDao?

    companion object {
        private const val DB_NAME = "Lab1DB"
        private var dbInstance: AppDatabase? = null

        // Zapewnia jedno połączenie do bazy danych na całą aplikację (singleton)
        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (dbInstance == null) {
                dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DB_NAME
                )
                    // Pozwala na wykonywanie zapytań w głównym wątku
                    .allowMainThreadQueries()
                    // W przypadku nieudanej migracji, baza danych zostanie zresetowana
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return dbInstance
        }
    }
}
