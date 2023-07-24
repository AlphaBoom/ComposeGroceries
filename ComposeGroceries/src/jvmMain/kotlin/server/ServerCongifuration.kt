package server

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import java.awt.Desktop
import java.net.URI

@Composable
@Preview
fun ServerConfiguration() {
    var showDirPicker by remember { mutableStateOf(false) }
    val serverState: ServerState by ServerManager.state.collectAsState()
    LaunchedEffect(Unit) {
        ServerManager.refreshState()
    }
    Surface(modifier = Modifier.padding(16.dp)) {
        DirectoryPicker(showDirPicker) { path ->
            showDirPicker = false
            path?.run { ServerConfig.serverPath = this }
        }
        Column {
            Row(modifier = Modifier.clickable {
                showDirPicker = true
            }) {
                Text("当前服务工作目录：${ServerConfig.serverPath}")
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(36.dp)) {
                Text("当前服务状态：${
                    when (serverState) {
                        ServerOffline -> "离线"
                        ServerOnline -> "在线"
                        ServerChecking -> "检查中"
                        ServerUnknown -> "未知"
                    }
                }")
                Spacer(Modifier.width(16.dp))
                when (serverState) {
                    ServerOnline -> OutlinedButton(onClick = {
                        ServerManager.stop()
                    }, shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            contentPadding = PaddingValues(8.dp)) {
                        Text("关闭", fontSize = 10.sp)
                    }

                    ServerOffline -> OutlinedButton(onClick = {
                        ServerManager.start()
                    }, shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            contentPadding = PaddingValues(8.dp)) {
                        Text("启动", fontSize = 10.sp)
                    }

                    else -> {}
                }
            }
            if (serverState.isRunning) {
                Text("点击查看API文档", color = Color.Blue, modifier = Modifier.clickable {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(URI("http://localhost:9000/docs"))
                    }
                })
            }
        }
    }
}