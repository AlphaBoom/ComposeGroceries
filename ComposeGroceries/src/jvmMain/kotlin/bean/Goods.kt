package bean

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class Goods(val name:String, val type:GoodsType, val icon:ImageVector)

enum class GoodsType{
    HelloWorld,
    LearnDesign,
    ChatBot,
    DrawNest,
    WaterEffect,
}