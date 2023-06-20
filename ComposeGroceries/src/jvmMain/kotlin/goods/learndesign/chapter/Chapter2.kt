package goods.learndesign.chapter

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val purple = Color(93, 28, 60)

@Composable
@Preview
fun Chapter_2_Section_1() {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.size(width = 480.dp, height = 240.dp)) {
            Box(modifier = Modifier.background(color = Color(198, 206, 195)).padding(8.dp, 8.dp)) {
                Text(
                    "Sock and Buskin",
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = purple
                )
                Text(
                    "Ambrosia Sidney",
                    modifier = Modifier.align(Alignment.TopStart),
                    color = purple
                )
                Text("(505)555-1212", modifier = Modifier.align(Alignment.TopEnd), color = purple)
                Text("109 Friday Street", modifier = Modifier.align(Alignment.BottomStart), color = purple)
                Text("Penshurst, NM", modifier = Modifier.align(Alignment.BottomEnd), color = purple)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "你的眼睛是不是停过5次？当然，这张小小的名片上放置了5项孤立的内容。\n 你是从哪里开始的？可能是从中间，因为中间的短句字体最粗。\n 接下来看什么？是不是按从左向右的顺序读？（因为这是英语。）\n如果已经读到名片的最后（即右下角），你的目光又会移向哪里？\n你是不是还会全盘再巡视一番，确保自己没有遗漏任何角落？",
            lineHeight = 1.5.em,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview
fun Chapter_2_Section_2() {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(backgroundColor = Color(198, 206, 195), modifier = Modifier.size(width = 480.dp, height = 240.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp, 8.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Sock and Buskin",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = purple
                )
                Text(
                    "Ambrosia Sidney",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = purple
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("109 Friday Street", color = purple)
                Text("Penshurst, NM", color = purple)
                Text("(505)555-1212", color = purple)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "如果多个项相互之间有很近的亲密性，它们就会成为一个视觉单元，而不是多个孤立的元素。就像实际生活中一样，亲密性（即紧密性）意味着存在关联。如果把类似的元素组织为一个单元，马上会带来很多变化。首先，页面会变得更有条理。其次，你会清楚地知道从哪里开始读信息，而且明白什么时候结束。另外，“空白”（字母以外的空间）也会变得更有组织。",
            lineHeight = 1.5.em,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun DotText(text: String, dotWidth: Dp) {
    Row {
        Text("·", textAlign = TextAlign.Center, modifier = Modifier.width(dotWidth))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
@Preview
fun Chapter_2_Section_3(dotWidth: Dp = 36.dp, desc: String = "") {
    Column(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.border(1.dp, Color.Black).padding(8.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "AREAS OF EXPERTISE",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    DotText("Strategic Planning and Execution", dotWidth)
                    DotText("Internet and New Media Development", dotWidth)
                    DotText("User Experience Improvements", dotWidth)
                    DotText("Software and Internet UX Design", dotWidth)

                }
                Column(modifier = Modifier.weight(1f)) {
                    DotText("Market and Consumer Research", dotWidth)
                    DotText("New Product Development and Launch", dotWidth)
                    DotText("Process Design and Reengineering", dotWidth)
                    DotText("Organizational Turnarounds", dotWidth)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            desc.ifEmpty {
                "亲密性的使用很微妙，不过相当重要。布局的时候一定要明确元素和其所属元属是" +
                        "否在一起，留意无关元素。\n请注意这两栏中的项目符号，看看它们和相关联的项目间的距离。中间的" +
                        "项目符号事实上离有些左栏的项目更近。看起来几乎就像是 4 个单独的列" +
                        "一样，其中有两列是项目符号。"
            },
            lineHeight = 1.5.em,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview
fun Chapter_2_Section_4() {
    Chapter_2_Section_3(
        dotWidth = 16.dp, desc = "现在关系就清晰了，我们可以立刻分辨出哪些项目符号属于哪些项目，也" +
                "可以马上看出有两列项目清单，而不是一列项目符号，一些信息，一列项" +
                "目符号，再来更多的信息。"
    )
}

@Composable
@Preview
fun Chapter_2_Section_5() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row {
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.body1.merge(TextStyle(fontFamily = FontFamily.Cursive))) {
                Surface(
                    contentColor = Color(53, 95, 120),
                    color = Color(242, 245, 238),
                    elevation = 8.dp,
                    modifier = Modifier.weight(1f).height(320.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "First Friday Club",
                            fontSize = 18.sp,
                            letterSpacing = -1.sp,
                            modifier = Modifier.scale(scaleX = 1f, scaleY = 1.8f).padding(8.dp)
                        )
                        Text(
                            "Winter Reading Schedule",
                            fontSize = 12.sp,
                            letterSpacing = -1.sp,
                            modifier = Modifier.scale(scaleX = 1f, scaleY = 1.2f).padding(4.dp)
                        )
                        Text(buildAnnotatedString {
                            append("Friday November 1 at 5 p.m.")
                            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                append("Cymbeline")
                            }
                        }, fontSize = 9.sp)
                        Text(
                            "In this action-packed drama, our strong and true\n"
                                    + "heroine,Imogen,dresses as a boy and runs off to a\n"
                                    + "cave in Wales to avoid marring a man she hates.\n"
                                    + "Friday, December 6,5 p.m. The Winter's Tale\n"
                                    + "The glorious Paulina and the steadfast Hermione\n"
                                    + "keep a secret together for sixteen years, util the\n"
                                    + "Delphic Oracle is proven true and the long-lost\n"
                                    + "daughter is found.\n"
                                    + "all readings held at the Mermaid Tavern. Spon-\n"
                                    + "sored by I Read Shakespeare.\n"
                                    + "Join us for $3\n"
                                    + "For seating information phone 555-1212\n"
                                    + "Also Friday, January 3 at 5 p.m. Twelfth Night\n"
                                    + "Join us as Olivia survives a shipwreck, dresses as\n"
                                    + "a man, gets a job, and finds both a man and a\n"
                                    + "woman in love with her.",
                            textAlign = TextAlign.Center,
                            lineHeight = 12.sp,
                            fontSize = 9.sp
                        )
                    }

                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    contentColor = Color(53, 95, 120),
                    color = Color(242, 245, 238),
                    elevation = 8.dp,
                    modifier = Modifier.weight(1f).height(320.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            "First Friday Club",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = -1.sp,
                            modifier = Modifier.scale(1f, 1.8f).padding(bottom = 8.dp)
                        )
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Text("Winter Reading Schedule", fontSize = 16.sp, letterSpacing = -1.sp)
                            Spacer(Modifier.height(6.dp))
                            Column {
                                Text("Cymbeline", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "In this action-packed drama, our strong and true heroine,Imogen,dresses as a boy and runs off to a cave in Wales to avoid marring a man she hates.",
                                    fontSize = 6.sp,
                                    color = Color.Black
                                )
                                Text("November 1 · Friday · 5 PM", fontSize = 8.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Column {
                                Text("The Winter's Tale", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "The glorious Paulina and the steadfast Hermione keep a secret together for sixteen years, util the Delphic Oracle is proven true and the long-lost daughter is found.",
                                    fontSize = 6.sp,
                                    color = Color.Black
                                )
                                Text("November 1 · Friday · 5 PM", fontSize = 8.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Column {
                                Text("Twelfth Night", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "Join us as Olivia survives a shipwreck, dresses as a man, gets a job, and finds both a man and a woman in love with her.",
                                    fontSize = 6.sp,
                                    color = Color.Black
                                )
                                Text("January 3 · Friday · 5 PM", fontSize = 8.sp)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column {
                                Text("The Mermaid Tavern", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "all readings held at the Mermaid Tavern.\n Sponsored by I Read Shakespeare.\n For seating information phone 555-1212\nJoin us for $3",
                                    fontSize = 5.sp,
                                    color = Color.Black
                                )
                            }
                        }

                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "左图，这个系列活动组织了多少场读书会？\n" +
                    "右图，这个系列活动组织了多少场读书会？\n" +
                    "我们看一眼就能回答右边的传单上列了多少场读书会，因为每场读书会的信息都被" +
                    "归入有逻辑的亲密关系中（另外，活动名称现在加粗了，使用的是对比原则）。请" +
                    "注意，三场读书会之间的空白是相同的，显示出这三组之间有某些关系。就算文本" +
                    "字号小到不能阅读，你也马上就能知道有三个活动。\n" +
                    "虽然传单底部的小块文字小到无法阅读，你也知道那是什么，对不对？一定是售票" +
                    "信息和联系方式。你马上就知道这个单元并不是另一场活动，因为它和其他区块之" +
                    "间的亲密性是不同的。",
            lineHeight = 1.5.em,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview
fun Chapter_2_Section_6() {
    Column {
        Text("亲密性小结", color = Color(131, 10, 55), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(buildAnnotatedString {
            append("如果多个项相互之间存在很近的")
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                append("亲密性")
            }
            append("，它们将成为一个视觉单元，而不是多个孤")
            append("立的元素。彼此相关的项应当归在一组。要有意识地注意你是怎样阅读的，你的视")
            append("线怎样移动：从哪里开始；沿着怎样的路径；到哪里结束；读完之后，接下来看哪")
            append("里？整个过程应当是一个合理的过程，有确定的开始，而且要有确定的结束。")
        })
        Spacer(modifier = Modifier.height(8.dp))
        Text("根本目的", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("亲密性的根本目的是实现组织性。尽管其他原则也能达到这个目的，不过利用亲密" +
                "性原则，只需简单地将相关的元素分在一组建立更近的亲密性，就能自动实现条理" +
                "性和组织性。如果信息很有条理，将更容易阅读，也更容易被记住。此外还有一个" +
                "很好的“副产品”，利用亲密性原则，还可以使空白（这是设计者们最喜欢的）更美" +
                "观（也更有条理）。")
        Spacer(modifier = Modifier.height(8.dp))
        Text("如何实现", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("微微眯起眼睛，统计眼睛停顿的次数来数一数页面上有多少个元素。如果页面上的" +
                "项超过 3 ～ 5 个（当然，这取决于具体情况），就要看看哪些孤立的元素可以归在一" +
                "组建立更近的亲密性，使之成为一个视觉单元。")
        Spacer(modifier = Modifier.height(8.dp))
        Text("要避免的问题", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("避免一个页面上有太多孤立的元素。" +
                "不要在元素之间留出同样大小的空白，除非各组同属于一个子集。" +
                "标题、子标题、图表标题、图片能否归入其相关材料？在这个问题上一定要非常清" +
                "楚（哪怕只有一点含糊都要尽量避免）。在有很近亲密性的元素之间建立关系。" +
                "不同属一组的元素之间不要建立关系！如果元素彼此无关，要把它们分开。" +
                "不要仅仅因为有空白就把元素放在角落或中央。")
    }
}