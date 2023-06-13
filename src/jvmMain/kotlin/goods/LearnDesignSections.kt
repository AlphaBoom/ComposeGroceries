package goods

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        Card(backgroundColor = Color(198, 206, 195),modifier = Modifier.size(width = 480.dp, height = 240.dp)) {
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
fun Chapter_2_Section_3() {
    Text("Chapter 1 Section 3")
}