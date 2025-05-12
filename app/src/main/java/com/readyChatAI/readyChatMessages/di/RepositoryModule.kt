package com.readyChatAI.readyChatMessages.di

import com.readyChatAI.readyChatMessages.data.repository.ChatRepositoryImpl
import com.readyChatAI.readyChatMessages.data.repository.PreferencesRepositoryImpl
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import com.readyChatAI.readyChatMessages.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSmsRepository(
        smsRepository: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        preferencesRepository: PreferencesRepositoryImpl
    ): PreferencesRepository
}