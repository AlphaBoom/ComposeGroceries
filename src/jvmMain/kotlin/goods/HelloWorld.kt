package goods

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@Preview
fun HelloWorld() {
    var text by remember { mutableStateOf("Hello, World!") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}