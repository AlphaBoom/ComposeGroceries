package goods

import AppViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.google.cloud.texttospeech.v1.Voice
import tts.TtsManager
import tts.TtsPlayer

@Composable
fun Item(text: String) {
    val player = remember { TtsPlayer(text) }
    val ttsState by player.state.collectAsState()
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
    Row(Modifier.padding(4.dp)) {
        Text(text, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        IconButton(onClick = {
            player.play()
        }) {
            if (ttsState.isPlaying) {
                Icon(Icons.Filled.Stop, "Stop")
            } else {
                Icon(Icons.Filled.VolumeUp, "Volume up")
            }

        }
        Spacer(Modifier.width(4.dp))
    }
}

@Composable
@Preview
fun ClipboardTTS() {
    val appState by AppViewModel.appState.collectAsState()
    val clipboardValue = remember { mutableStateListOf<String>() }
    var supportedVoiceList: List<Voice> by remember { mutableStateOf(emptyList()) }
    var voiceFilter by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        supportedVoiceList = TtsManager.getSupportVoiceList()
        AppViewModel.clipboardHistory.collect {
            clipboardValue.add(it)
        }
    }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("是否监听剪贴板")
            Checkbox(appState.clipboardTTS, onCheckedChange = {
                AppViewModel.enableClipboardTTS(it)
            })
        }
        Row {
            Spacer(Modifier.width(8.dp))
            Box {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(modifier = Modifier.width(240.dp).onFocusChanged {
                    if (!it.hasFocus) {
                        expanded = false
                    }
                }, value = if (expanded) {
                    voiceFilter
                } else {
                    TtsManager.currentVoiceName
                }, onValueChange = {
                    expanded = true
                    voiceFilter = it
                }, label = { Text("当前声音") }, maxLines = 1)
                DropdownMenu(expanded, offset = DpOffset(66.dp, 0.dp),
                        focusable = false,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.requiredHeightIn(max = 360.dp)) {
                    supportedVoiceList.map { voice -> voice.name }.filter { it.contains(voiceFilter) }.forEach {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            TtsManager.setCurrentVoice(voiceName = it)
                        }) {
                            Text(it)
                        }
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            Box {
                var expanded by remember { mutableStateOf(false) }
                Text("当前语言：${TtsManager.currentLanguageCode}", modifier = Modifier.clickable {
                    expanded = true
                })
                DropdownMenu(expanded, offset = DpOffset(66.dp, 0.dp), onDismissRequest = { expanded = false }, modifier = Modifier.requiredHeightIn(max = 360.dp)) {
                    supportedVoiceList.map { voice -> voice.getLanguageCodes(0) }.toSet().forEach {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            TtsManager.setCurrentVoice(languageCode = it)
                        }) {
                            Text(it)
                        }
                    }
                }
            }
        }
        if (appState.clipboardTTS) {
            LazyColumn {
                items(clipboardValue) {
                    Item(text = it)
                }
            }
        }
    }
}