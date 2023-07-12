package factory

import androidx.compose.runtime.Composable
import bean.Goods
import bean.GoodsType
import goods.ClipboardTTS
import goods.DrawNest
import goods.chat.ChatBot
import goods.HelloWorld
import goods.WaterEffect
import goods.learndesign.LearnDesign

object GoodsFactory {
    fun createGoodsComposable(goods:Goods): @Composable ()->Unit{
        return {
            when(goods.type) {
                GoodsType.HelloWorld -> HelloWorld()
                GoodsType.LearnDesign -> LearnDesign()
                GoodsType.ChatBot -> ChatBot()
                GoodsType.DrawNest -> DrawNest()
                GoodsType.WaterEffect -> WaterEffect()
                GoodsType.ClipboardTTS -> ClipboardTTS()
            }
        }
    }
}