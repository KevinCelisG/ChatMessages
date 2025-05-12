package com.readyChatAI.readyChatMessages.data.receiver

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import com.readyChatAI.readyChatMessages.util.Constants
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SmsSentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            RESULT_OK ->
                Log.d(Constants.DEBUG_TAG, "SMS sent")

            SmsManager.RESULT_ERROR_GENERIC_FAILURE ->
                Log.d(Constants.DEBUG_TAG, "Error")

            SmsManager.RESULT_ERROR_NO_SERVICE ->
                Log.d(Constants.DEBUG_TAG, "No service")

            SmsManager.RESULT_ERROR_NULL_PDU ->
                Log.d(Constants.DEBUG_TAG, "PDU null")

            SmsManager.RESULT_ERROR_RADIO_OFF ->
                Log.d(Constants.DEBUG_TAG, "Radio off")
        }

        val repository = EntryPointAccessors.fromApplication(
            context.applicationContext,
            SmsRepositoryEntryPoint::class.java
        ).smsRepository()

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            repository.informAboutNewMessages(value = true)
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SmsRepositoryEntryPoint {
        fun smsRepository(): ChatRepository
    }
}