package com.example.lab1.database

import io.reactivex.rxjava3.core.Observable
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab1.User
import io.reactivex.rxjava3.core.Completable

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Completable

    @Query("SELECT * FROM user") // Adjusted to match the table name in the User entity.
    fun getAllUsers(): Observable<List<User>>
}
