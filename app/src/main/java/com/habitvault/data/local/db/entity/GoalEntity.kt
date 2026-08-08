package com.habitvault.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "goals",
    indices = [Index(value = ["habit_id"])]
)
data class GoalEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "habit_id")
    val habitId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "target_type")
    val targetType: String,

    @ColumnInfo(name = "target_value")
    val targetValue: Int,

    @ColumnInfo(name = "deadline")
    val deadline: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "achieved_at")
    val achievedAt: Long?,
)
