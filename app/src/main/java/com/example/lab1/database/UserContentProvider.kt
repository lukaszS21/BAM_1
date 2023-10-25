package com.example.lab1.database

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.lab1.User

// Definicja dostawcy zawartości dla encji User umożliwiająca interakcję z bazą danych za pomocą URI
class UserContentProvider : ContentProvider() {
    // Referencja do bazy danych
    private lateinit var database: AppDatabase

    // Inicjalizacja dostawcy zawartości i bazy danych
    override fun onCreate(): Boolean {
        database = AppDatabase.getInstance(context!!)!!
        return true
    }

    // Obsługa zapytań do bazy danych z wynikami w postaci kursora
    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val users: List<User> = database.userDao()?.getAllUsers()?.blockingFirst() ?: listOf()
        val cursor = MatrixCursor(arrayOf("id", "username", "stopperValue"))
        for (user in users) {
            cursor.addRow(arrayOf<Any>(user.id, user.username, user.stopperValue))
        }
        return cursor
    }

    // Pozostałe metody (wstawianie, aktualizacja, usuwanie, getType)
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun getType(uri: Uri): String? = null
}
