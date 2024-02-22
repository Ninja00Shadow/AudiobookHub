package com.example.audiobookhub.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditText(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 24.sp,
                color = Color.Gray
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 24.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EditTextPreview() {
    EditText("", {}, "Placeholder")
}

@Composable
fun EditTextInteger(value: Int, onValueChange: (Int) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = if(value == 0) "" else value.toString(),
        onValueChange = { temp ->
            val text = temp.filter { it.isDigit() }
            onValueChange(text.toIntOrNull() ?: 0)
        },
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 24.sp,
                color = Color.Gray
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 24.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EditTextIntegerPreview() {
    EditTextInteger(0, {}, "Placeholder")
}

@Composable
fun RatingBar(
    rating: Float = 5f,
    maxRating: Int = 5,
    onStarClick: (Int) -> Unit,
    isIndicator: Boolean = false
) {
    Row {
        for (i in 1..maxRating) {
            if (i <= rating.toInt()) {
                // Full stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(!isIndicator) {
                            onStarClick(i)
                        }
                )
            } else if (i == rating.toInt() + 1 && rating % 1 != 0f) {
                // Partial star
                PartialStar(fraction = rating % 1)
            } else {
                // Empty stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(!isIndicator) {
                            onStarClick(i)
                        }
                )
            }
        }
    }
}

@Composable
private fun PartialStar(fraction: Float) {
    val customShape = FractionalClipShape(fraction)

    Box {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(50.dp)
        )
        Box(
            modifier = Modifier
                .graphicsLayer(
                    clip = true,
                    shape = customShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}


private class FractionalClipShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RatingBarPreview() {
    RatingBar(
        rating = 3.5f,
        maxRating = 5,
        onStarClick = {},
    )
}

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp),
    ) {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 2.dp),
                    fontSize = textStyle.fontSize,
                    fontWeight = textStyle.fontWeight,
                    color = textStyle.color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioButtonGroupPreview() {
    RadioButtonGroup(listOf("Option 1", "Option 2", "Option 3"), "Option 1") {}
}