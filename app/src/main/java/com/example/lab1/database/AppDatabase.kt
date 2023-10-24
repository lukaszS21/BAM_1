package com.example.lab1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab1.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?

    companion object {
        private const val DB_NAME = "Lab1DB"
        private var dbInstance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (dbInstance == null) {
                dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return dbInstance
        }
    }
}
