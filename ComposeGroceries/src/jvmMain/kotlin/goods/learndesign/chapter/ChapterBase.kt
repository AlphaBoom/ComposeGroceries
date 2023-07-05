package goods.learndesign.chapter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun ChapterConclusion(title:String, content:String="", rootPurpose:String="", howToImplement:String="", avoidProblem:String=""){
    Column {
        Text(title, color = Color(131, 10, 55), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(content)
        Spacer(modifier = Modifier.height(8.dp))
        Text("根本目的", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(rootPurpose)
        Spacer(modifier = Modifier.height(8.dp))
        Text("如何实现", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(howToImplement)
        Spacer(modifier = Modifier.height(8.dp))
        Text("要避免的问题", color = Color(131, 10, 55), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(avoidProblem)
    }
}