package com.readyChatAI.readyChatMessages.presentation.screens.categories

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.domain.model.Category
import com.readyChatAI.readyChatMessages.presentation.components.header.HeaderComponent
import com.readyChatAI.readyChatMessages.presentation.components.items.CategoryItem
import com.readyChatAI.readyChatMessages.util.Constants

@Composable
fun CategoriesScreen(
    categoryViewModel: CategoryViewModel
) {
    val context = LocalContext.current

    val categoryState = categoryViewModel.state

    var showDialogToCreateOrUpdate by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }

    var showDialogToDelete by remember { mutableStateOf(false) }
    var currentCategory by remember { mutableStateOf<Category?>(null) }

    var nameCategory by remember { mutableStateOf("") }
    var newKeyword by remember { mutableStateOf("") }
    val keywords = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(
            titleScreen = context.getString(R.string.categories_title),
            onClickListener = {
                currentCategory = null
                nameCategory = ""
                keywords.clear()

                isUpdating = false
                showDialogToCreateOrUpdate = true
            }
        )

        HorizontalDivider(
            modifier = Modifier
                .height(10.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(categoryState.listCategories.size) { i ->
                CategoryItem(
                    category = categoryState.listCategories[i],
                    onEditClick = {
                        currentCategory = it
                        nameCategory = it.name
                        keywords.clear()
                        keywords.addAll(it.listKeywords)

                        isUpdating = true
                        showDialogToCreateOrUpdate = true
                    },
                    onDeleteClick = {
                        currentCategory = it
                        showDialogToDelete = true
                    }
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            onClick = {
                currentCategory = null
                nameCategory = ""
                keywords.clear()

                isUpdating = false
                showDialogToCreateOrUpdate = true
            }
        ) {
            Text(context.getString(R.string.add_new_category))
        }

        if (showDialogToCreateOrUpdate) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = if (isUpdating)
                            context.getString(R.string.edit_category)
                        else
                            context.getString(R.string.create_category),
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = nameCategory,
                            onValueChange = {
                                nameCategory = it
                            },
                            label = { Text(context.getString(R.string.name)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newKeyword,
                                onValueChange = { newKeyword = it },
                                label = { Text(context.getString(R.string.new_keyword)) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (newKeyword.isNotBlank()) {
                                        keywords.add(newKeyword)
                                        newKeyword = ""
                                    }
                                }
                            ) {
                                Text(context.getString(R.string.add))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(keywords) { kw ->

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    ) {
                                        Text(
                                            text = kw,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription =
                                            context.getString(
                                                R.string.delete_item,
                                                kw
                                            ),
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    keywords.remove(kw)
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nameCategory.isNotBlank() && keywords.isNotEmpty()) {
                                currentCategory =
                                    Category(
                                        name = nameCategory,
                                        listKeywords = keywords,
                                        id = currentCategory?.id ?: 0
                                    )

                                currentCategory?.let {
                                    categoryViewModel.onEvent(
                                        if (isUpdating) {
                                            CategoryEvent.UpdateCategory(
                                                it
                                            )
                                        } else {
                                            CategoryEvent.CreateCategory(
                                                it
                                            )
                                        }
                                    )
                                }

                                showDialogToCreateOrUpdate = false
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.fill_all_fields),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(
                            if (isUpdating) context.getString(R.string.edit_category) else context.getString(
                                R.string.create_category
                            )
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialogToCreateOrUpdate = false
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        if (showDialogToDelete) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.delete_item,
                            currentCategory?.name.orEmpty()
                        ),
                        fontSize = 20.sp
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            id = R.string.sure_delete_item,
                            currentCategory?.name.orEmpty()
                        ),
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Start
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            categoryViewModel.onEvent(
                                CategoryEvent.DeleteCategory(
                                    currentCategory?.id ?: 0
                                )
                            )
                            showDialogToDelete = false
                        }
                    ) {
                        Text(stringResource(id = R.string.delete))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialogToDelete = false
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }
    }

    LaunchedEffect(categoryState.newCategories) {
        if (categoryState.newCategories) {
            categoryViewModel.onEvent(CategoryEvent.GetCategories)
        }
    }

    LaunchedEffect(Unit) {
        if (categoryState.listCategories.isEmpty()) {
            categoryViewModel.onEvent(CategoryEvent.GetCategories)
        }
    }

    BackHandler {
        (context as Activity).finish()
    }
}

