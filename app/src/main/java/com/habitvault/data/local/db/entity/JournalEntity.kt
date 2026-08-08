package com.habitvault.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "journal_entries",
    indices = [
        Index(value = ["date"], unique = true),
        Index(value = ["habit_id"]),
    ]
)
data class JournalEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "habit_id")
    val habitId: String?,

    @ColumnInfo(name = "mood_score")
    val moodScore: Int?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
