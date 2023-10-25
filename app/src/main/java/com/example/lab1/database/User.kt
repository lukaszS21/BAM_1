package com.example.lab1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Definicja encji User reprezentującej użytkownika w bazie danych
@Entity(tableName = User.TABLE_NAME)
class User(
    @field:ColumnInfo(name = "username") var username: String,
    @field:ColumnInfo(name = "value") var stopperValue: Int
) {
    // Automatycznie generowany klucz główny dla każdego użytkownika
    @PrimaryKey(autoGenerate = true)
    var id = 0

    // Reprezentacja tekstowa obiektu User
    override fun toString(): String {
        return "{id=" + id +
                ", username='" + username + '\'' +
                ", stopperValue=" + stopperValue +
                '}'
    }

    companion object {
        // Nazwa tabeli w bazie danych
        const val TABLE_NAME = "user"
    }
}
