package com.habitvault.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "completions",
    indices = [
        Index(value = ["habit_id", "date"]),
        Index(value = ["date"]),
    ]
)
data class CompletionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "habit_id")
    val habitId: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "note")
    val note: String?,
)
