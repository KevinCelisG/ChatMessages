package com.readyChatAI.readyChatMessages.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val repository = EntryPointAccessors.fromApplication(
                context.applicationContext,
                SmsRepositoryEntryPoint::class.java
            ).smsRepository()

            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                repository.informAboutNewMessages(value = true)
            }
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SmsRepositoryEntryPoint {
        fun smsRepository(): ChatRepository
    }
}