import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bean.Goods
import bean.GoodsType
import factory.GoodsFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

private val goods = arrayListOf(
    Goods("Hello World", GoodsType.HelloWorld, Icons.Rounded.Home),
    Goods("Learn Design", GoodsType.LearnDesign, Icons.Rounded.Favorite),
    Goods("Chat Bot", GoodsType.ChatBot, Icons.Rounded.AccountBox),
    Goods("Draw Nest", GoodsType.DrawNest, Icons.Rounded.AccountCircle),
    Goods("Water effect", GoodsType.WaterEffect, Icons.Rounded.ArrowForward),
    Goods("Clipboard TTS", GoodsType.ClipboardTTS, Icons.Rounded.ThumbUp)
)

private val mutableWindowEvents = MutableStateFlow<KeyEvent?>(null)

val windowEvents: Flow<KeyEvent> = mutableWindowEvents.filterNotNull()

@Composable
@Preview
fun App(goods: Goods) {
    GoodsFactory.createGoodsComposable(goods)()
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    application {
        var currentGoods by remember { mutableStateOf(goods[1]) }
        val appState by AppViewModel.appState.collectAsState()
        if (appState.clipboardTTS) {
            Tray(
                rememberVectorPainter(Icons.Rounded.ThumbUp),
                onAction = {
                    AppViewModel.showPanel()
                },
                menu = {
                    Item("Exit") {
                        exitApplication()
                    }
                }
            )
        }
        if (appState.showPanel) {
            Window(
                icon = painterResource("logo.png"),
                onCloseRequest = {
                    AppViewModel.handleCloseRequest {
                        exitApplication()
                    }
                },
                title = "Compose Groceries",
                onPreviewKeyEvent = {
                    mutableWindowEvents.value = it
                    false
                }
            ) {
                MenuBar {
                    Menu("Shelves") {
                        goods.forEach {
                            Item(it.name, enabled = it != currentGoods, onClick = {
                                currentGoods = it
                            })
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    MaterialTheme {
                        App(currentGoods)
                    }
                }
            }
        }
    }

}
