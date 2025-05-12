package com.readyChatAI.readyChatMessages.di

import android.app.Application
import android.telephony.SmsManager
import androidx.room.Room
import com.readyChatAI.readyChatMessages.data.local.ReadyChatMessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSmsManager(): SmsManager = SmsManager.getDefault()

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ReadyChatMessagesDatabase {
        return Room.databaseBuilder(app, ReadyChatMessagesDatabase::class.java, "readychat.db")
            .fallbackToDestructiveMigration().build()
    }
}