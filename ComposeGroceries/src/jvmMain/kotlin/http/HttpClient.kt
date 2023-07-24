package http

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.sse.RealEventSource
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object HttpClient {
    private const val HOST = "http://localhost:9000"
    private val chatClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()
    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(HOST)
                .build()
    }

    val commonService by lazy {
        retrofit.create(CommonService::class.java)
    }

    fun postMessage(message:String) : Flow<String> {
        val request = Request.Builder().url("$HOST/chat/$message").build()
        return callbackFlow {
            val realEventSource = RealEventSource(request, object:EventSourceListener(){
                override fun onOpen(eventSource: EventSource, response: Response) {
                }

                override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                    trySend(data)
                }

                override fun onClosed(eventSource: EventSource) {
                    close()
                }

                override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                    println("onFailure:${t?.message ?: ""}")
                }
            })
            realEventSource.connect(chatClient)
            awaitClose {
                realEventSource.cancel()
            }
        }
    }

}