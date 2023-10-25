package com.example.lab1.database

import io.reactivex.rxjava3.core.Observable
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab1.User
import io.reactivex.rxjava3.core.Completable

// Definicja interfejsu DAO dla encji User, umożliwiająca dostęp do bazy danych
@Dao
interface UserDao {
    // Dodawanie użytkownika do bazy danych
    @Insert
    fun insert(user: User): Completable

    // Pobieranie wszystkich użytkowników z bazy danych
    @Query("SELECT * FROM user")
    fun getAllUsers(): Observable<List<User>>
}
