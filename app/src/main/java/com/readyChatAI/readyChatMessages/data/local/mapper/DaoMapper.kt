package com.readyChatAI.readyChatMessages.data.local.mapper

import com.google.gson.Gson
import com.readyChatAI.readyChatMessages.data.local.entities.CategoryEntity
import com.readyChatAI.readyChatMessages.domain.model.Category

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        keywords = Gson().toJson(listKeywords)
    )
}

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        listKeywords = Gson().fromJson(keywords, Array<String>::class.java).toList()
    )
}
