package com.readyChatAI.readyChatMessages.data.repository

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.readyChatAI.readyChatMessages.data.local.ReadyChatMessagesDatabase
import com.readyChatAI.readyChatMessages.data.local.mapper.toCategory
import com.readyChatAI.readyChatMessages.data.local.mapper.toCategoryEntity
import com.readyChatAI.readyChatMessages.data.util.getContactNameFromNumber
import com.readyChatAI.readyChatMessages.domain.model.Category
import com.readyChatAI.readyChatMessages.domain.model.Chat
import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.domain.model.Sender
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import com.readyChatAI.readyChatMessages.util.Resource
import com.readyChatAI.readyChatMessages.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val smsManager: SmsManager,
    localDB: ReadyChatMessagesDatabase
) : ChatRepository {

    private val dao = localDB.dao

    private var chatList = mutableListOf<Chat>()
    private var categoriesList = mutableListOf<Category>()

    private val _areNewMessages = MutableLiveData<Boolean>()
    override val areNewMessages: LiveData<Boolean> = _areNewMessages

    override suspend fun informAboutNewMessages(value: Boolean) {
        _areNewMessages.postValue(value)
    }

    override fun sendSms(phoneNumber: String, message: String) {
        val intent = Intent("com.readyChatAI.SMS_SENT_ACTION").apply {
            `package` = context.packageName
        }

        val sentIntent = PendingIntent.getBroadcast(
            context, 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            sentIntent,
            null
        )

    }

    override suspend fun getChats(): Flow<Resource<List<Chat>>> {
        return flow {
            emit(Resource.Loading(true))

            val chats = mutableListOf<Chat>()

            loadMessages(isSentByMe = false, chats, context)
            loadMessages(isSentByMe = true, chats, context)

            chatList = chats
            synchronizeCategories()

            emit(Resource.Loading(false))
            emit(Resource.Successful(chatList.toList()))
        }.catch {
            emit(Resource.Loading(false))
            emit(Resource.Error("Error getting messages", emptyList()))
        }
    }

    private fun synchronizeCategories() {
        val keywordToCategoryMap = buildKeywordMap(categoriesList)

        for (chat in chatList) {
            addCategoriesToChat(chat, keywordToCategoryMap)
        }
    }

    private fun buildKeywordMap(listCategories: List<Category>): Map<String, String> {
        return listCategories
            .flatMap { category ->
                category.listKeywords.map { keyword ->
                    keyword.lowercase() to category.name
                }
            }
            .toMap()
    }

    private fun addCategoriesToChat(
        chat: Chat,
        keywordToCategoryMap: Map<String, String>
    ) {
        val foundCategories = chat.listMessages
            .flatMap { message ->
                message.text
                    .split(Regex("\\s+"))
                    .map { it.trim().lowercase() }
            }
            .mapNotNull { word ->
                keywordToCategoryMap[word]
            }
            .distinct()

        chat.listCategories = foundCategories.toMutableList()
    }

    private fun loadMessages(isSentByMe: Boolean, chats: MutableList<Chat>, context: Context) {
        val cursor = context.contentResolver.query(
            Uri.parse(if (isSentByMe) "content://sms/sent" else "content://sms/inbox"),
            arrayOf(
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
            ),
            null,
            null,
            "${Telephony.Sms.DATE} DESC LIMIT 100"
        )

        cursor?.use {
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)

            var total = 0

            while (it.moveToNext()) {
                total++
                val phoneNumber = it.getString(addressIndex)
                val text = it.getString(bodyIndex)
                val timestamp = it.getLong(dateIndex)

                if (!text.isNullOrEmpty()) {
                    val message = Message(
                        text = text,
                        isSent = isSentByMe,
                        date = Instant.ofEpochMilli(timestamp)
                    )

                    chats.firstOrNull { chat ->
                        Util.advancedMatch(
                            chat.sender.phoneNumber,
                            phoneNumber
                        )
                    }
                        ?.listMessages
                        ?.add(message)
                        ?: chats.add(
                            Chat(
                                sender = Sender(
                                    name = context.getContactNameFromNumber(phoneNumber),
                                    phoneNumber = phoneNumber
                                ),
                                listMessages = mutableListOf(message)
                            )
                        )
                }
            }
        }
    }

    override suspend fun createCategory(category: Category): Flow<Resource<Category>> {
        return flow {
            emit(Resource.Loading(true))

            dao.createCategory(category.toCategoryEntity())

            emit(Resource.Successful(data = category))

            emit(Resource.Loading(false))
        }.catch {
            emit(Resource.Error("Error creating category"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getCategories(): Flow<Resource<List<Category>>> {
        return flow {
            emit(Resource.Loading(true))

            val listCategories = mutableListOf<Category>()

            for (category in dao.getCategories()) {
                listCategories.add(category.toCategory())
            }

            categoriesList = listCategories
            synchronizeCategories()
            informAboutNewMessages(true)

            emit(Resource.Successful(data = listCategories.toList()))

            emit(Resource.Loading(false))
        }.catch {
            emit(Resource.Error("Error getting categories"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun updateCategory(category: Category): Flow<Resource<Category>> {
        return flow {
            emit(Resource.Loading(true))

            dao.updateCategory(category.toCategoryEntity())

            emit(Resource.Successful(data = category))

            emit(Resource.Loading(false))
        }.catch {
            emit(Resource.Error("Error updating category"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun deleteCategory(categoryId: Long): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading(true))

            dao.deleteCategoryById(categoryId)

            emit(Resource.Successful(data = categoryId))

            emit(Resource.Loading(false))
        }.catch {
            emit(Resource.Error("Error updating category"))
            emit(Resource.Loading(false))
        }
    }
}