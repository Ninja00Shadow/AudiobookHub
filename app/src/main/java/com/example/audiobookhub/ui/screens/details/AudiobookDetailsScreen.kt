package com.example.audiobookhub.ui.screens.details

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audiobookhub.R
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.ui.BottomPlayer
import com.example.audiobookhub.ui.RatingBar

@Composable
fun AudiobookDetailsScreen(
    audiobook: AudioBook,
    formattedDuration: String,
    score: Int,
    onScoreChanged: (Int) -> Unit,
    goBack: () -> Unit,
    isPlaying: Boolean,
    progress: Float,
    onPlayPause: () -> Unit,
    timeRemaining: String,
    onPlayerClick: () -> Unit
) {
    Scaffold (
        bottomBar = {
            BottomPlayer(
                audiobook = audiobook,
                isPlaying = isPlaying,
                progress = progress,
                onPlayPause = onPlayPause,
                timeRemaining = timeRemaining,
                onClick = onPlayerClick
            )
        }
    ) {
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier
                            .size(26.dp)
                            .clickable {
                                goBack()
                            },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = audiobook.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    bitmap = audiobook.getCover() ?: BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                        R.drawable.no_image_available
                    ).asImageBitmap(),
                    contentDescription = "Cover",
                )

                Text(
                    text = "Author: ${audiobook.author}",
                    fontSize = 18.sp,
                )

                Text(
                    text = "Reads: ${audiobook.narrator}",
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.height(10.dp))

                RatingBar(
                    rating = score.toFloat(),
                    isIndicator = true,
                    onStarClick = {
                        onScoreChanged(it)
                    },
                )

                Text(
                    text = "Duration: $formattedDuration",
                )

                Spacer(modifier = Modifier.height(10.dp))

                Divider()

                Text(
                    text = audiobook.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAudiobookDetailsScreen() {
    AudiobookDetailsScreen(
        audiobook = AudioBook(
            chapterFolderName = "",
            title = "Ogień Przebudzenia",
            author = "Anthony Ryan",
            narrator = "Joanna Domańska",
            score = 4,
            cover = BitmapFactory.decodeResource(
                LocalContext.current.resources,
                R.drawable.ogien_przebudzenia_cover
            ),
            duration = 100000,
            progress = 0.5f,
            playbackSpeed = 1.0f,
            description = "",
            chapters = emptyList()
        ),
        formattedDuration = "10h 10m",
        score = 4,
        onScoreChanged = {},
        goBack = {},
        isPlaying = true,
        progress = 50f,
        onPlayPause = {},
        timeRemaining = "10h 10m",
        onPlayerClick = {}
    )
}