package com.habitvault.di

import android.content.Context
import androidx.room.Room
import com.habitvault.data.local.db.HabitVaultDatabase
import com.habitvault.data.local.db.dao.CompletionDao
import com.habitvault.data.local.db.dao.GoalDao
import com.habitvault.data.local.db.dao.HabitDao
import com.habitvault.data.local.db.dao.JournalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HabitVaultDatabase {
        return Room.databaseBuilder(
            context,
            HabitVaultDatabase::class.java,
            HabitVaultDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideHabitDao(database: HabitVaultDatabase): HabitDao = database.habitDao()

    @Provides
    fun provideCompletionDao(database: HabitVaultDatabase): CompletionDao = database.completionDao()

    @Provides
    fun provideGoalDao(database: HabitVaultDatabase): GoalDao = database.goalDao()

    @Provides
    fun provideJournalDao(database: HabitVaultDatabase): JournalDao = database.journalDao()
}
