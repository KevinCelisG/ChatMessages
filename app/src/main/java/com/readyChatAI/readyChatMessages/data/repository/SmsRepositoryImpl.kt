package com.readyChatAI.readyChatMessages.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsManager
import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.domain.repository.SmsRepository
import com.readyChatAI.readyChatMessages.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val smsManager: SmsManager
) : SmsRepository {

    override fun sendSms(phoneNumber: String, message: String) {
        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            null,
            null
        )
    }

    @SuppressLint("Recycle")
    override fun getMessages(): Flow<Resource<List<Message>>> {
        return flow {
            emit(Resource.Loading(true))

            val messages = mutableListOf<Message>()
            val cursor = context.contentResolver.query(
                Uri.parse("content://sms/inbox"),
                arrayOf(
                    Telephony.Sms.ADDRESS,
                    Telephony.Sms.BODY,
                    Telephony.Sms.DATE
                ),
                null,
                null,
                "${Telephony.Sms.DATE} DESC LIMIT 50"
            )

            cursor?.use {
                val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
                val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
                val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)

                while (it.moveToNext()) {
                    val sender = it.getString(addressIndex)
                    val text = it.getString(bodyIndex)
                    val timestamp = it.getLong(dateIndex)

                    if (!text.isNullOrEmpty()) {
                        messages.add(
                            Message(
                                text = text,
                                isSent = false,
                                sender = sender,
                                date = Instant.ofEpochMilli(timestamp)
                            )
                        )
                    }
                }
            }

            emit(Resource.Loading(false))
            emit(Resource.Successful(messages.toList()))
        }.catch {
            emit(Resource.Loading(false))
            emit(Resource.Error("Error getting messages", emptyList()))
        }
    }
}