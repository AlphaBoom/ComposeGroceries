package goods.chat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun ChatBot() {
    val viewModel = remember { ChatBotViewModel() }
    val state by viewModel.state.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    Column {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
            coroutineScope.launch {
                listState.animateScrollToItem(state.messageList.size - 1, 0)
            }
            items(state.messageList.size, { it }) {
                Column {
                    val msg = state.messageList[it]
                    Text(msg.type.value)
                    Text(msg.message)
                }
            }
        }
        TextField(inputText, {
            inputText = it
        }, singleLine = true, modifier = Modifier.onKeyEvent {
            if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                viewModel.postMessage(inputText.text)
                inputText = TextFieldValue()
                true
            } else {
                false
            }
        }.fillMaxWidth())
    }
}