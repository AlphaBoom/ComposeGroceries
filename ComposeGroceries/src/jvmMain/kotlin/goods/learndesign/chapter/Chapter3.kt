package goods.learndesign.chapter

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.SupportFontFamilyType
import utils.loadFont

@Composable
@Preview
fun Chapter_3_Section_1() {
    val clarendon = remember { loadFont(SupportFontFamilyType.Clarendon) }
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp)) {
        Text(
            "居中对齐是初学者最常用的对齐方式，这种" +
                    "对齐看起来很安全，感觉上也很舒服。居中对齐会创建一种更正式、更稳重的外" +
                    "观，这种外观显得更为中规中矩，但通常也很乏味。请注意你喜欢的那些设计。我" +
                    "敢保证，大多数看来精巧的设计都没有采用居中对齐。我知道，作为一个初学者，" +
                    "要完全摒弃居中对齐会很难，但你必须从一开始就强制自己避开它。通过充分利用" +
                    "亲密性，并结合明确的右对齐或左对齐，你会惊异于设计的改观。"
        )
        Spacer(Modifier.height(18.dp))
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = Color(251, 247, 234),
                    elevation = 12.dp,
                    modifier = Modifier.fillMaxWidth().height(360.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.height(54.dp))
                        Text("A Return to the", fontFamily = clarendon)
                        Text("Great Variety of Readers", fontFamily = clarendon)
                        Text("The History and Future")
                        Text("of Reading Shakespeare")
                        Spacer(Modifier.height(24.dp))
                        Text("by")
                        Text("Patricia May Williams")
                        Spacer(Modifier.height(24.dp))
                        Text("February 26")
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    """
                    这是一个很典型的报告封面，是不是？
                    这种标准格式提供了乏味的外观，
                    表现出几乎是一种业余水准，这会
                    影响别人对整个报告的第一印象。
                """.trimIndent().replace("\n", "")
                )
            }
            Spacer(Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = Color(251, 247, 234),
                    elevation = 12.dp,
                    modifier = Modifier.fillMaxWidth().height(360.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                        Text("A Return to the", fontFamily = clarendon, fontSize = 16.sp)
                        Text("Great Variety of Readers", fontFamily = clarendon, fontSize = 16.sp)
                        Text("The History and Future", fontSize = 12.sp)
                        Text("of Reading Shakespeare", fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        Text("Patricia May Williams", fontSize = 12.sp)
                        Text("February 26", fontSize = 12.sp)
                        Spacer(Modifier.height(24.dp))
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    """
                    利用明确的左对齐，可以使这个
                    报告封面给人留下更精美的印象。
                    尽管作者的名字离题目很远，但
                    那条看不见的线提供了强有力的
                    对齐基准，可以将这两个文本块
                    连在一起。
                """.trimIndent().replace("\n", "")
                )
            }
        }
    }
}

@Composable
@Preview
fun Chapter_3_Section_2() {
    val amorieModella = remember { loadFont(SupportFontFamilyType.AmorieModella) }
    Column {
        Text(
            """
            并不是建议你绝对不要居中！只是要留意这种居中对齐的效果，这真的是你想要
            表达的效果吗？当然，有时候确实如此，例如，大多数婚礼都很庄重、很正式，所
            以，如果你想用居中方式设计结婚喜帖，完全可以在营造喜庆的同时有意这么做。
        """.trimIndent()
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    elevation = 8.dp,
                ) {
                    Box(
                        modifier = Modifier.size(180.dp, 200.dp).background(Color(215, 172, 83)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Please join us\non March 14\nfor Pie Day!",
                            textAlign = TextAlign.Center,
                            color = Color(40, 79, 81),
                            fontSize = 40.sp,
                            fontFamily = amorieModella,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    """
                    居中。感觉很稳固，甚至感
                    觉非常乏味，就算字体可爱
                    也没用。
                """.trimIndent()
                )
            }
            Spacer(Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    elevation = 16.dp,
                ) {
                    Box(
                        modifier = Modifier.size(180.dp, 200.dp).background(Color(215, 172, 83)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Please\ndo join us\non March 14 for\nPie Day!",
                            textAlign = TextAlign.Center,
                            color = Color(40, 79, 81),
                            fontSize = 40.sp,
                            fontFamily = amorieModella,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    """
                    如果想让文本居中，至少让
                    它明显一点！
                """.trimIndent()
                )
            }
            Spacer(Modifier.width(16.dp))
        }
        Spacer(Modifier.height(16.dp))
        Row {
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    elevation = 8.dp,
                ) {
                    Box(
                        modifier = Modifier.size(180.dp, 200.dp).background(Color(215, 172, 83))
                    ) {
                        Image(
                            painterResource("branch_1.svg"), null,
                            colorFilter = ColorFilter.tint(Color(219, 182, 110)),
                            modifier = Modifier.layout { measurable, constraints ->
                                val size = (constraints.maxHeight * 0.95).toInt()
                                val placeable = measurable.measure(Constraints.fixed(size, size))
                                layout(placeable.width, placeable.height) {
                                    placeable.placeRelative(-48.dp.roundToPx(), 24.dp.roundToPx())
                                }
                            }.rotate(-140f)
                        )
                        Text(
                            "Please\ndo join us\non March 14\nfor Pie Day!",
                            textAlign = TextAlign.Center,
                            color = Color(40, 79, 81),
                            fontSize = 36.sp,
                            fontFamily = amorieModella,
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    """
                    试试看，包含居中文本的块
                    不要居中。
                """.trimIndent()
                )
            }
            Spacer(Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    elevation = 8.dp,
                ) {
                    Box(
                        modifier = Modifier.size(180.dp, 200.dp).background(Color(215, 172, 83))
                    ) {
                        Image(
                            painterResource("branch_2.svg"), null,
                            colorFilter = ColorFilter.tint(Color(219, 182, 110)),
                            modifier = Modifier.layout { measurable, constraints ->
                                val size = (constraints.maxHeight * 1.1f).toInt()
                                val placeable = measurable.measure(Constraints.fixed(size, size))
                                layout(placeable.width, placeable.height) {
                                    placeable.placeRelative(-36.dp.roundToPx(), 0)
                                }
                            }.scale(scaleX = 0.6f, scaleY = 1f).rotate(-90f)
                        )
                        Text(
                            "Please\njoin us\non\nMarch 14\nfor\nPie Day!",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = amorieModella,
                            modifier = Modifier.align(Alignment.TopEnd)
                                .padding(end = 16.dp)
                                .background(Color(29, 83, 93))
                                .padding(4.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    """
                    如果想让文本居中，尝试一下
                    用另外某种方式使它更生动。
                """.trimIndent()
                )
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview
fun Chapter_3_Section_3() {
    val roswell = remember { loadFont(SupportFontFamilyType.RoswellTwoItc) }
    val warnock = remember { loadFont(SupportFontFamilyType.WarnockPro) }
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            """
            有时，你可能喜欢在同一个页面上同时使用右对齐和左对齐文本，不过一定要确保
            让这些文本以某种方式对齐！
        """.trimIndent()
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Surface(
                color = Color(253, 245, 232),
                elevation = 8.dp,
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.size(240.dp, 360.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("One Night in Winnemucca", fontFamily = roswell, fontSize = 48.sp)
                    Text("Jerry Caveglia", fontFamily = warnock, fontStyle = FontStyle.Italic, fontSize = 18.sp)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "A saga\nof adventure\nand romance\nand one moccasin",
                        fontFamily = warnock,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Text(
                """
                在这个例子中，标题和子标题都
                是左对齐，不过文字介绍是居中
                的，两个文本元素之间没有共同
                的对齐方式。它们相互之间没有
                任何联系。
            """.trimIndent()
            )
        }
        Spacer(Modifier.height(16.dp))
        Row {
            Surface(
                color = Color(253, 245, 232),
                elevation = 8.dp,
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.size(240.dp, 360.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "One Night in Winnemucca",
                        fontFamily = roswell,
                        fontSize = 48.sp,
                        modifier = Modifier.alignBy { it.measuredWidth })
                    Text("Jerry Caveglia", fontFamily = warnock, fontStyle = FontStyle.Italic, fontSize = 18.sp)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "A saga\nof adventure\nand romance\nand one moccasin",
                        fontFamily = warnock,
                        textAlign = TextAlign.End,
                        modifier = Modifier.alignBy {
                            it.measuredWidth
                        }
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Text(
                """
                尽管这两个元素仍然采用两种不
                同的对齐方式（上面是左对齐，
                下面是右对齐），但是下面介绍文
                本的右边界与上方标题的右边界
                对齐，这就用一条看不见的线把
                二者连接起来。
                训练你的眼睛找到那条隐形的线。
            """.trimIndent()
            )
        }
    }
}

@Composable
@Preview
fun Chapter_3_Section_4() {
    ChapterConclusion(
        "对齐小结",
        """
            任何元素都不能在页面上随意摆放。每个元素应当与页面上的另外一个元素存在某
            种视觉联系。
            在设计中，统一性是一个重要的概念。要让页面上的所有元素看上去统一、有联系
            而且彼此相关，需要在各个单独的元素之间存在某种视觉纽带。尽管这些孤立元素
            在页面上的物理位置可能并不靠近，但是通过适当放置，可以让它们看上去是有联
            系而且相关的，并且与其他信息统一。可以看看你喜欢的那些设计。一个精美的设
            计不论最初看上去多么杂乱无章，总能找出其中的对齐方式。
        """.trimIndent(),
        """
            对齐的根本目的是使页面统一而且有条理。其效果类似于把客厅里四处零落的洋娃
            娃捡起来，并把它们放在一个玩具箱中。
            不论创建精美的、正式的、有趣的还是严肃的外观，通常都可以利用一种明确的对
            齐（当然，要结合适当的字体）来达到目的。
        """.trimIndent(),
        """
            要特别注意元素放在哪里。应当总能在页面上找出与之对齐的元素，尽管这两个对
            象的物理位置可能相距很远。
        """.trimIndent(),
        """
            要避免在页面上混合使用多种文本对齐方式（也就是说，不要将某些文本居中，而
            另外一些文本右对齐）。
            另外，要着力避免居中对齐，除非你有意识地想要创建一种比较正式、稳重（通常
            也更乏味）的表示。并不是完全杜绝使用居中对齐，有时可以有意地选择这种对齐
            方式，但是不要把它作为默认选择。
        """.trimIndent()
    )
}