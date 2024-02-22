package com.example.audiobookhub.ui.screens.playerScreen

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.example.audiobookhub.R
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.data.model.Chapter
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    speed: Float,
    onSpeedChange: (Float) -> Unit,
    chapterRemaining: String,
    timeElapsed: String,
    timeRemaining: String,
    isPlaying: Boolean,
    audioBook: AudioBook,
    currentChapter: Chapter,
    onStart: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    onItemClick: (Int) -> Unit,
    onMore: () -> Unit
) {
    var openChapterDialog by remember { mutableStateOf(false) }
    var openSpeedDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = audioBook.title,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(start = 10.dp),
            )

            IconButton(
                onClick = { onMore() }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .requiredSize(300.dp)
                .padding(horizontal = 15.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = audioBook.getCover(),
                contentDescription = "Cover",
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currentChapter.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
//                    text = "${currentChapter.getDurationLeft(chapterProgress)} remaining",
//                    text = "${currentChapter.getDurationLeft(progress)} remaining",
                    text = "$chapterRemaining remaining",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            IconButton(onClick = {
                openChapterDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.FormatListBulleted,
                    contentDescription = "Chapters"
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

//        Slider(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 10.dp, end = 10.dp),
//            value = progress,
//            onValueChange = { onProgress(it) },
//            valueRange = 0f..100f
//        )
        SeekBarWithDelay(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            seekBarValue = progress,
            valueRange = 0f..100f,
            onSeekBarValueChanged = { onProgress(it)}
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = timeElapsed,
            )

            Text(
                text = timeRemaining,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable { openSpeedDialog = true },
                text = String.format("%.2fx", speed),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            IconButton(
                onClick = onBackward
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.time15backward),
                    contentDescription = "Stop/Start",
                )
            }

            PlayerIconItem(
                icon = if (isPlaying) Icons.Default.Pause
                else Icons.Default.PlayArrow
            ) {
                onStart()
            }

            IconButton(
                onClick = onForward
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.time15forward),
                    contentDescription = "Forward 10 seconds",
                )
            }

            Spacer(modifier = Modifier.size(30.dp))
        }
    }

    if (openChapterDialog) {
        ChapterDialog(
            onDismissRequest = { openChapterDialog = false },
            currentChapter = currentChapter,
            chapters = audioBook.chapters,
            onItemClick = {
                openChapterDialog = false
                onItemClick(it)
            }
        )
    }


    if (openSpeedDialog) {
        SpeedDialog(
            onDismissRequest = { openSpeedDialog = false },
            speed = speed,
            onSpeedChange = {
                openSpeedDialog = false
                onSpeedChange(it)
            }
        )
    }
}

@Composable
fun SeekBarWithDelay(
    modifier: Modifier = Modifier,
    seekBarValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onSeekBarValueChanged: (Float) -> Unit
) {
    var seekPosition by remember { mutableFloatStateOf(seekBarValue) }

    LaunchedEffect(seekPosition) {
        delay(300)
        onSeekBarValueChanged(seekPosition)
    }

    Slider(
        modifier = modifier,
        value = seekPosition,
        onValueChange = { newValue ->
            seekPosition = newValue
        },
        valueRange = valueRange
    )
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    PlayerScreen(
        progress = 40f,
        onProgress = {},
        speed = 1f,
        onSpeedChange = {},
        chapterRemaining = "01:00",
        timeElapsed = "01:06:40",
        timeRemaining = "01:40:00",
        isPlaying = true,
        audioBook = AudioBook(
            chapterFolderName = "Sample Folder",
            title = "Sample Title",
            author = "Sample Author",
            narrator = "Sample Narrator",
            score = 3,
            cover = null,
            duration = 10000000,
            description = "Sample Description",
            chapters = listOf(
                Chapter(
                    uri = "".toUri(),
                    name = "Chapter 1",
                    1,
                    duration = 100000
                )
            )
        ),
        currentChapter = Chapter(
            uri = "".toUri(),
            name = "Chapter 1",
            1,
            duration = 100000
        ),
        onStart = {},
        onForward = {},
        onBackward = {},
        onItemClick = {},
        onMore = {}
    )
}

@Composable
fun ChapterDialog(
    onDismissRequest: () -> Unit,
    currentChapter: Chapter,
    chapters: List<Chapter>,
    onItemClick: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Chapters",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(chapters) { index, chapter ->
                    if (chapter == currentChapter) {
                        ChapterListItem(
                            chapter = chapter,
                            isCurrent = true,
                            onItemClick = { onItemClick(index) }
                        )
                    } else {
                        ChapterListItem(
                            chapter = chapter,
                            onItemClick = { onItemClick(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterListItem(
    chapter: Chapter,
    isCurrent: Boolean = false,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clickable {
                onItemClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) MaterialTheme.colorScheme.secondary else Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = chapter.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = chapter.getDurationLeft(0f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Preview
@Composable
fun ChapterDialogPreview() {
    ChapterDialog(
        onDismissRequest = {},
        currentChapter = Chapter(
            uri = "".toUri(),
            name = "Chapter 1",
            1,
            duration = 200000
        ),
        chapters = listOf(
            Chapter(
                uri = "".toUri(),
                name = "Chapter 1",
                1,
                duration = 200000
            ),
            Chapter(
                uri = "".toUri(),
                name = "Chapter 2",
                1,
                duration = 100000
            ),
        ),
        onItemClick = {}
    )
}

@Composable
fun SpeedDialog(
    onDismissRequest: () -> Unit,
    speed: Float,
    onSpeedChange: (Float) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Speed",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(10.dp),
                    textAlign = TextAlign.Center
                )

                val options = listOf("1.0x", "1.25x", "1.5x", "1.75x", "2.0x")
                val selectedIndex = speed.toString() + "x"

                SpeedRadioButtonGroup(
                    options = options,
                    selectedOption = selectedIndex,
                    onOptionSelected = {
                        onSpeedChange(it.dropLast(1).toFloat())
                    }
                )

            }

        }
    }
}

@Composable
fun SpeedRadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 80.dp),
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 2.dp),
                    fontSize = textStyle.fontSize,
                    fontWeight = textStyle.fontWeight,
                    color = textStyle.color
                )

                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeedDialogPreview() {
    SpeedDialog(
        onDismissRequest = {},
        speed = 1f,
        onSpeedChange = {}
    )
}