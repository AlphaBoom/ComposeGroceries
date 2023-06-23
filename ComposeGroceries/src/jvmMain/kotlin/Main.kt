import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bean.Goods
import bean.GoodsType
import factory.GoodsFactory

val mGoods = arrayListOf(
    Goods("Hello World", GoodsType.HelloWorld, Icons.Rounded.Home),
    Goods("Learn Design", GoodsType.LearnDesign, Icons.Rounded.Favorite),
    Goods("Chat Bot", GoodsType.ChatBot, Icons.Rounded.AccountBox),
    Goods("Draw Nest", GoodsType.DrawNest, Icons.Rounded.AccountCircle),
    Goods("Water effect", GoodsType.WaterEffect, Icons.Rounded.ArrowForward)
)

@Composable
fun GoodsDetail(goods: Goods) {
    Surface(modifier = Modifier.padding(16.dp)) {
        GoodsFactory.createGoodsComposable(goods)()
    }
}

@Composable
fun GoodsShelves(selectedItem: Int, onItemClick: (Int, Goods) -> Unit) {
    NavigationRail {
        mGoods.forEachIndexed { index, goods ->
            Surface {
                NavigationRailItem(
                    selected = selectedItem == index,
                    onClick = { onItemClick(index, goods) },
                    icon = { Icon(goods.icon, contentDescription = null) },
                    label = { Text(goods.name) },
                    modifier = Modifier.size(width = 140.dp, height = 72.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun App() {
    var selectedItem by remember { mutableStateOf(0) }
    var currentGoods by remember { mutableStateOf(mGoods[selectedItem]) }
    Row {
        GoodsShelves(selectedItem) { index, goods ->
            selectedItem = index
            currentGoods = goods
        }
        GoodsDetail(currentGoods)
    }
}

fun main() {
    application {
        Window(
            icon = painterResource("logo.png"),
            onCloseRequest = ::exitApplication,
            title = "Compose Groceries"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    App()
                }
            }
        }
    }

}
