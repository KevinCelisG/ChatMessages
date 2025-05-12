package com.readyChatAI.readyChatMessages.data.util

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

fun Context.getContactNameFromNumber(phoneNumber: String?): String? {
    if (phoneNumber.isNullOrEmpty()) return null

    return try {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )

        contentResolver.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(0)
            } else {
                null
            }
        }
    } catch (e: Exception) {
        null
    }
}

fun Context.getContactNameFromUri(uri: Uri): String? {
    return try {
        contentResolver.query(
            uri,
            arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(0)
            } else {
                null
            }
        }
    } catch (e: Exception) {
        null
    }
}

fun Context.getPhoneNumberFromUri(uri: Uri): String? {
    var phoneNumber: String? = null

    // Primero obtenemos el ID del contacto desde el URI
    val contactId = try {
        contentResolver.query(
            uri,
            arrayOf(ContactsContract.Contacts._ID),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getLong(0)
            } else {
                null
            }
        }
    } catch (e: Exception) {
        null
    }

    // Consultamos los números asociados al contacto
    contactId?.let { id ->
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

        contentResolver.query(
            phoneUri,
            projection,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(id.toString()),
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                phoneNumber = cursor.getString(0)
            }
        }
    }

    return phoneNumber
}