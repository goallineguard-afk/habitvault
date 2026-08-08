package com.habitvault.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.habitvault.data.local.db.dao.CompletionDao
import com.habitvault.data.local.db.dao.GoalDao
import com.habitvault.data.local.db.dao.HabitDao
import com.habitvault.data.local.db.dao.JournalDao
import com.habitvault.data.local.db.entity.CompletionEntity
import com.habitvault.data.local.db.entity.GoalEntity
import com.habitvault.data.local.db.entity.HabitEntity
import com.habitvault.data.local.db.entity.JournalEntity

@Database(
    entities = [HabitEntity::class, CompletionEntity::class, GoalEntity::class, JournalEntity::class],
    version = 1,
    exportSchema = true
)
abstract class HabitVaultDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun completionDao(): CompletionDao
    abstract fun goalDao(): GoalDao
    abstract fun journalDao(): JournalDao

    companion object {
        const val DATABASE_NAME = "habitvault.db"
    }
}
