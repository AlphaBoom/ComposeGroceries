package goods.learndesign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collect
import windowEvents

@Composable
fun Chapter(name: String, scrollState: ScrollState, section: @Composable () -> Unit) {
    Column {
        Text(name, style = MaterialTheme.typography.h5)
        Surface(Modifier.weight(1f).padding(top = 8.dp).verticalScroll(scrollState)) {
            section()
        }
    }
}

@Composable
fun ChapterCatalog(viewModel: LearnDesignViewModel){
    val uiState by viewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier.width(120.dp).padding(horizontal = 4.dp, vertical = 8.dp)) {
        itemsIndexed(viewModel.chapters) { index, chapter ->
            Text(
                text = chapter.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().background(
                    if (uiState.chapterIndex == index) {
                        Color.LightGray
                    } else {
                        Color.Transparent
                    }
                ).clickable(uiState.chapterIndex != index, onClick = {
                    viewModel.setChapterIndex(index)
                }).padding(vertical = 2.dp)
            )
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun LearnDesign() {
    val viewModel: LearnDesignViewModel = remember { LearnDesignViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollState by rememberUpdatedState(remember(uiState){ ScrollState(0) })
    LaunchedEffect(Unit) {
        windowEvents.collect {
            if (it.type != KeyEventType.KeyDown) {
                return@collect
            }
            when (it.key) {
                Key.DirectionLeft -> {
                    viewModel.pre()
                }

                Key.DirectionRight-> {
                    viewModel.next()
                }

                Key.DirectionUp -> {
                    scrollState.scrollBy(-200f)
                }

                Key.DirectionDown -> {
                    scrollState.scrollBy(200f)
                }
            }
        }
    }
    Row {
        ChapterCatalog(viewModel)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            Surface(modifier = Modifier.weight(1f)) {
                Chapter(uiState.chapterName, scrollState, uiState.section)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                AnimatedVisibility(uiState.hasPre) {
                    Button(onClick = { viewModel.pre() }, modifier = Modifier.padding(horizontal = 2.dp)) {
                        Text("pre")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AnimatedVisibility(uiState.hasNext) {

                    Button(onClick = { viewModel.next() }, modifier = Modifier.padding(horizontal = 2.dp)) {
                        Text("next")
                    }
                }
            }
        }
    }
}