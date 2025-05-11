package com.readyChatAI.readyChatMessages.di

import com.readyChatAI.readyChatMessages.data.repository.SmsRepositoryImpl
import com.readyChatAI.readyChatMessages.domain.repository.SmsRepository
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
        smsRepository: SmsRepositoryImpl
    ): SmsRepository
}