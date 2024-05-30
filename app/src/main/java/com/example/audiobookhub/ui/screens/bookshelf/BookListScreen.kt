package com.example.audiobookhub.ui.screens.bookshelf

import android.content.res.Resources.Theme
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.audiobookhub.R
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.ui.BottomPlayer

@Composable
fun BookListScreen(
    books: List<AudioBook>,
    onBookSelected: (AudioBook) -> Unit,
    onDetailsClick: (AudioBook) -> Unit,
    currentBook: AudioBook?,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    timeRemaining: String,
    onBottomPlayerClick: () -> Unit,
    onAddBook: () -> Unit
) {
    Scaffold (
        bottomBar = {
            if (currentBook != null) {
                BottomPlayer(
                    audiobook = currentBook,
                    isPlaying = isPlaying,
                    progress = currentBook.progress,
                    onPlayPause = onPlayPause,
                    timeRemaining = timeRemaining,
                    onClick = onBottomPlayerClick
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddBook()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new Audiobook from files"
                )
            }
        }
    ) {
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
            ) {
                itemsIndexed (books) { index, book ->
                    BookCard(
                        book = book,
                        onBookSelected = onBookSelected,
                        onDetailsClick = onDetailsClick
                    )
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: AudioBook,
    onDetailsClick: (AudioBook) -> Unit,
    onBookSelected: (AudioBook) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card (
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onBookSelected(book)
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .requiredSize(150.dp),
                model = book.cover,
                contentDescription = "Book cover",
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        LinearProgressIndicator(
            progress = book.progress / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = book.title,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )

            Icon(
                modifier = Modifier.clickable { onDetailsClick(book) },
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Show details",
            )
        }
    }
}

@Preview
@Composable
fun BookListScreenPreview() {
    val book = AudioBook(
        chapterFolderName = "chapterFolderName",
        title = "Ogień Przebudzenia",
        author = "author",
        narrator = "narrator",
        score = 5,
        cover = "".toUri(),
        duration = 0,
        progress = 50f,
        playbackSpeed = 1f,
        description = "description",
        chapters = emptyList(),
    )
    
    BookListScreen(
        books = listOf(
            book,
        ),
        onBookSelected = {},
        onDetailsClick = {},
        currentBook = book,
        isPlaying = true,
        onPlayPause = {},
        timeRemaining = "1:30:00",
        onBottomPlayerClick = {},
        onAddBook = {}
    )
}