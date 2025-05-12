package com.readyChatAI.readyChatMessages.presentation.components.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readyChatAI.readyChatMessages.domain.model.Category

@Composable
fun CategoryTypeItem(
    category: Category,
    isSelected: Boolean,
    onClick: (Category) -> Unit
) {
    val color =
        if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
    val fontColor =
        if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground

    Button(
        border = BorderStroke(
            1.dp, fontColor
        ),
        colors = ButtonDefaults.buttonColors(color),
        onClick = {
            onClick(category)
        }
    ) {
        Text(
            text = category.name,
            color = fontColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}