package com.example.lab1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = User.TABLE_NAME)
class User(
    @field:ColumnInfo(name = "username") var username: String,
    @field:ColumnInfo(name = "value") var stopperValue: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    override fun toString(): String {
        return "{id=" + id +
                ", username='" + username + '\'' +
                ", stopperValue=" + stopperValue +
                '}'
    }

    companion object {
        const val TABLE_NAME = "user"
    }
}
