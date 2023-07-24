package server

import http.HttpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import utils.retry
import java.io.File
import java.nio.file.Path
import java.util.prefs.Preferences
import kotlin.concurrent.thread


private const val SERVER_PATH = "ServerConfig.serverPath"

object ServerConfig {
    var serverPath: String
        get() = preference { get(SERVER_PATH, Path.of("").toAbsolutePath().toString()) }
        set(value) = preference { put(SERVER_PATH, value) }

    private fun <T> preference(block: Preferences.() -> T): T {
        return Preferences.userRoot().block()
    }
}

object ServerManager {
    private var _state = MutableStateFlow<ServerState>(ServerUnknown)

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val scope = CoroutineScope(newSingleThreadContext("ServerManager"))
    private var processId: String? = null
    val state = _state.asStateFlow()

    init {
        checkServiceStatus()
    }

    fun refreshState() {
        checkServiceStatus()
    }

    private fun checkServiceStatus(delay: Long = 0L) {
        scope.launch {
            _state.value = ServerChecking
            if (delay > 0) {
                delay(delay)
            }
            val pid: String? = retry(null, times = 3) {
                val response = HttpClient.commonService.isAlive()
                if (response.isSuccessful) {
                    response.body()?.string()
                } else {
                    throw HttpException(response)
                }
            }
            processId = pid
            println(processId)
            if (pid != null) {
                _state.value = ServerOnline
            } else {
                _state.value = ServerOffline
            }
        }
    }

    fun start() {
        val process = ProcessBuilder()
                .directory(File(ServerConfig.serverPath))
                .command("pipenv", "run", "python", "main.py")
                .redirectErrorStream(true).start()
        thread {
            process!!.inputStream.bufferedReader().use {
                while (true) {
                    val line = it.readLine() ?: break
                    println(line)
                }
            }
        }
        processId = process.pid().toString()
        println(processId)
        checkServiceStatus(delay = 1000L)
    }

    fun stop() {
        processId?.let { pid ->
            val process = Runtime.getRuntime().exec("taskkill /t /f /PID $pid")
            if (process.waitFor() == 0) {
                _state.value = ServerOffline
            } else {
                checkServiceStatus()
            }
        }
        processId = null
    }
}

sealed interface ServerState {
    val isRunning: Boolean
        get() = this is ServerOnline
}

object ServerOnline : ServerState
object ServerOffline : ServerState
object ServerUnknown : ServerState
object ServerChecking : ServerState