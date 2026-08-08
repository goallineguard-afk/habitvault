package com.habitvault.di

import com.habitvault.core.domain.repository.CompletionRepository
import com.habitvault.core.domain.repository.GoalRepository
import com.habitvault.core.domain.repository.HabitRepository
import com.habitvault.core.domain.repository.JournalRepository
import com.habitvault.data.repository.CompletionRepositoryImpl
import com.habitvault.data.repository.GoalRepositoryImpl
import com.habitvault.data.repository.HabitRepositoryImpl
import com.habitvault.data.repository.JournalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindCompletionRepository(impl: CompletionRepositoryImpl): CompletionRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    @Singleton
    abstract fun bindJournalRepository(impl: JournalRepositoryImpl): JournalRepository
}
