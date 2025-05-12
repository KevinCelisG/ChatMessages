package com.readyChatAI.readyChatMessages.presentation.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.readyChatAI.readyChatMessages.domain.model.Chat
import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.util.Util
import java.time.Instant

@Composable
fun ChatItem(
    chat: Chat,
    modifier: Modifier = Modifier,
    onItemClick: (Chat) -> Unit
) {
    val newestMessage = chat.listMessages.maxByOrNull { it.date } ?: Message(
        text = "",
        isSent = true,
        date = Instant.now()
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(
                color = if (newestMessage.isSent)
                    Color(0xFF4C7CCA)
                else
                    Color(0xFF349C76),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .clickable {
                onItemClick(chat)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = chat.sender.name ?: chat.sender.phoneNumber,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Text(
                text = Util.formattedDateText(newestMessage.date),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = newestMessage.text,
            style = MaterialTheme.typography.bodyLarge,
            color =  Color(0xFF2E3033),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chat.listCategories) { keyword ->
                KeywordChip(keyword)
            }
        }
    }
}
