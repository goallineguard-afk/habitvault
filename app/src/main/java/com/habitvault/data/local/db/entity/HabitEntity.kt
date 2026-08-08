package com.habitvault.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "frequency_type")
    val frequencyType: String,

    @ColumnInfo(name = "frequency_value")
    val frequencyValue: String?,

    @ColumnInfo(name = "color")
    val color: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "archived")
    val archived: Boolean,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int,

    @ColumnInfo(name = "reminder_time")
    val reminderTime: String?,
)
