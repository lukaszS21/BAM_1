package com.example.lab1.database

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.lab1.User

class UserContentProvider : ContentProvider() {
    private lateinit var database: AppDatabase

    override fun onCreate(): Boolean {
        database = AppDatabase.getInstance(context!!)!!
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val users: List<User> = database.userDao()?.getAllUsers()?.blockingFirst() ?: listOf()
        val cursor = MatrixCursor(arrayOf("id", "username", "stopperValue"))
        for (user in users) {
            cursor.addRow(arrayOf<Any>(user.id, user.username, user.stopperValue))
        }
        return cursor
    }

    // Other methods (insert, update, delete, getType) can return default values or null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun getType(uri: Uri): String? = null
}
