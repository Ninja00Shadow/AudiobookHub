package com.example.audiobookhub.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.audiobookhub.R
import com.example.audiobookhub.data.model.AudioBook


@Composable
fun BottomPlayer(
    audiobook: AudioBook,
    isPlaying: Boolean,
    progress: Float,
    onPlayPause: () -> Unit,
    timeRemaining: String,
    onClick: () -> Unit
) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row (
                modifier = Modifier
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Image(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .padding(4.dp),
//                    bitmap = audiobook.getCover() ?: BitmapFactory.decodeResource(
//                        LocalContext.current.resources,
//                        R.drawable.no_image_available
//                    ).asImageBitmap(),
//                    contentDescription = "Cover",
//                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp),
                    model = audiobook.cover,
                    contentDescription = "Cover"
                )

                Column (
                    modifier = Modifier
                        .wrapContentWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = audiobook.title,
                        fontSize = 15.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )

                    Text(
                        text = "Left: $timeRemaining",
                        fontSize = 13.sp
                    )
                }
            }

            LinearProgressIndicator(
                progress = progress /100f,
                modifier = Modifier
                    .height(4.dp)
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )

            IconButton(
                onClick = { onPlayPause() },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomPlayerPreview() {
    BottomPlayer(
        audiobook = AudioBook(
            chapterFolderName = "",
            title = "Ogień Przebudzenia",
            author = "",
            narrator = "",
            score = 4,
            cover = Uri.Builder()
                .scheme("res")
                .path(java.lang.String.valueOf(R.drawable.ogien_przebudzenia_cover))
                .build(),
            duration = 100000,
            progress = 0.5f,
            playbackSpeed = 1.0f,
            description = "",
            chapters = emptyList()
        ),
        isPlaying = true,
        progress = 50f,
        onPlayPause = {},
        timeRemaining = "1:30:00",
        onClick = {}
    )
}