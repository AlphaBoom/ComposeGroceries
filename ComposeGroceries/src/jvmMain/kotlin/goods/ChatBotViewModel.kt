package goods

import http.HttpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.toImmutableList

class ChatBotViewModel {
    private val messages = arrayListOf(Message(MessageType.ROBOT, "你好有什么可以帮助您的？"))
    private val _state = MutableStateFlow(genUiState())
    val state = _state.asStateFlow()

    fun postMessage(message: String) {
        messages.add(Message(MessageType.USER, message))
        _state.value = genUiState()
        GlobalScope.launch {
            HttpClient.postMessage(message).collect{
                when(messages.lastOrNull()?.type ?: MessageType.USER){
                    MessageType.ROBOT -> {
                        val msg = messages.last()
                        messages[messages.lastIndex] = Message(msg.type, "${msg.message}$it")
                    }
                    MessageType.USER -> {
                        messages.add(Message(MessageType.ROBOT, it))
                    }
                }
                _state.value = genUiState()
            }
        }
    }

    private fun genUiState():ChatBotUiState{
        return ChatBotUiState(ArrayList(messages))
    }
}

enum class MessageType(val value:String){
    ROBOT("Robot"),USER("User")
}

data class Message(val type:MessageType, val message:String)

data class ChatBotUiState(val messageList:List<Message>, val count:Int = messageList.size)


