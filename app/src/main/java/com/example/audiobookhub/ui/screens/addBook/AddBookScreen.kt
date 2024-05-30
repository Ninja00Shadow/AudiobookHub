package com.example.audiobookhub.ui.screens.addBook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddBookScreen(
    newBook: NewBook,
    fileType: FileTypes,
    filesPath: List<String>,
    goBack: () -> Unit,
    onUiEvents: (AddBookUiEvents) -> Unit,
) {
//    val filePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            onUiEvents(AddBookUiEvents.ChangeFilePath(uri))
//        }
//    }

    val filePickerLauncher = when(fileType){
        FileTypes.Mp3 -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uris: List<Uri>? ->
            if (uris != null) {
                onUiEvents(AddBookUiEvents.ChangeFilePath(uris))
            }
        }
        FileTypes.Zip -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                onUiEvents(AddBookUiEvents.ChangeFilePath(listOf(uri)))
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = newBook.title,
                onValueChange = {
                    onUiEvents(AddBookUiEvents.ChangeTitle(it))
                },
                enabled = filesPath.isNotEmpty(),
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = newBook.title.ifEmpty { "Title" },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = newBook.author,
                onValueChange = {
                    onUiEvents(AddBookUiEvents.ChangeAuthor(it))
                },
                enabled = filesPath.isNotEmpty(),
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = newBook.author.ifEmpty { "Author" },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = newBook.narrator,
                onValueChange = {
                    onUiEvents(AddBookUiEvents.ChangeNarrator(it))
                },
                enabled = filesPath.isNotEmpty(),
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = newBook.narrator.ifEmpty { "Narrator" },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = newBook.description,
                onValueChange = {
                    onUiEvents(AddBookUiEvents.ChangeDescription(it))
                },
                enabled = filesPath.isNotEmpty(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = false,
                placeholder = {
                    Text(
                        text = newBook.description.ifEmpty { "Description" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

            if (filesPath.isEmpty()) {
                Text(
                    text = "No file selected",
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            }
            else {
                Text(
                    text = filesPath.count().toString() + " file(s) selected",
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            }

            Button(
                onClick = {
                    if (fileType == FileTypes.Mp3)
                        filePickerLauncher.launch("audio/*")
                    else
                        filePickerLauncher.launch("application/zip")
                }
            )
            {
                Text("Select File")
            }

            Button(
                onClick = {
                    onUiEvents(AddBookUiEvents.SaveBook)
                    goBack()
                },
                enabled = filesPath.isNotEmpty() && newBook.title != "",
            ) {
                Text(text = "Save")
            }
        }

    }
}

@Preview
@Composable
fun AddBookScreenPreview() {
    AddBookScreen(
        newBook = NewBook("", "", "", ""),
        fileType = FileTypes.Mp3,
        filesPath = listOf(),
        goBack = {},
        onUiEvents = {}
    )
}