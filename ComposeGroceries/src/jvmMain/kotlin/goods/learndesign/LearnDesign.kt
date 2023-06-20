package goods.learndesign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.unit.dp

@Composable
fun Chapter(name: String, section: @Composable () -> Unit) {
    Column {
        Text(name, style = MaterialTheme.typography.h5)
        Surface(Modifier.weight(1f).padding(top = 8.dp).verticalScroll(rememberScrollState())) {
            section()
        }
    }
}

@Composable
@Preview
fun LearnDesign(viewModel: LearnDesignViewModel = remember { LearnDesignViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()
    Column {
        Surface(modifier = Modifier.weight(1f)) {
            Chapter(uiState.chapterName, uiState.section)
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            AnimatedVisibility(uiState.hasPre) {
                Button(onClick = { viewModel.pre() }) {
                    Text("pre")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(uiState.hasNext) {
                Button(onClick = { viewModel.next() }) {
                    Text("next")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLearnDesign() {
    LearnDesign()
}