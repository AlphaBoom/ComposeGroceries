import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tts.TtsManager
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.util.prefs.Preferences

private val KEY_CLIPBOARD_TTS = "KEY_CLIPBOARD_TTS"

object AppViewModel : ClipboardOwner {
    private val preferences = Preferences.userRoot()
    private val _appState = MutableStateFlow(AppState(clipboardTTS = preferences.getBoolean(KEY_CLIPBOARD_TTS, false)))
    private val _clipboardHistory = MutableSharedFlow<String>(5)
    private var _last: String? = null
    private var failedClipboardCnt = 0
    private var ttsServiceRegistered = false
    private var currentState
        get() = _appState.value
        set(value) {
            _appState.value = value
        }
    val appState = _appState.asStateFlow()
    val clipboardHistory = _clipboardHistory.asSharedFlow()

    init {
        if (currentState.clipboardTTS) {
            registerClipboardTtsService()
        }
    }

    private fun registerClipboardTtsService() {
        if (ttsServiceRegistered) {
            return
        }
        ttsServiceRegistered = true
        Toolkit.getDefaultToolkit().systemClipboard.apply {
            setContents(getContents(this@AppViewModel), this@AppViewModel)
        }
        try {
            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(object : NativeKeyListener {

                override fun nativeKeyTyped(nativeEvent: NativeKeyEvent) {
                    if (nativeEvent.keyChar == 'o' && (nativeEvent.modifiers and NativeKeyEvent.CTRL_MASK) != 0) {
                        playLatestTts()
                    }
                }

            })
            GlobalScreen.addNativeMouseListener(object :NativeMouseListener{
                override fun nativeMousePressed(nativeEvent: NativeMouseEvent) {
                    if (nativeEvent.button == 5) {
                        playLatestTts()
                    }
                }
            })
        } catch (e: NativeHookException) {
            e.printStackTrace()
        }
    }

    private fun unregisterClipboardTtsService() {
        if (!ttsServiceRegistered) {
            return
        }
        try {
            GlobalScreen.unregisterNativeHook()
        } catch (e: NativeHookException) {
            e.printStackTrace()
        }
        ttsServiceRegistered = false
    }

    fun enableClipboardTTS(enable: Boolean) {
        preferences.putBoolean(KEY_CLIPBOARD_TTS, enable)
        _appState.value = currentState.copy(clipboardTTS = enable)
        if (enable) {
            registerClipboardTtsService()
        } else {
            unregisterClipboardTtsService()
        }
    }

    fun showPanel() {
        currentState = currentState.copy(showPanel = true)
    }

    fun handleCloseRequest(block: () -> Unit) {
        if (currentState.clipboardTTS) {
            currentState = currentState.copy(showPanel = false)
        } else {
            block()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun playLatestTts() {
        _last?.let {
            GlobalScope.launch {
                TtsManager.play(-1, it)
            }
        }
    }

    override fun lostOwnership(clipboard: Clipboard, contents: Transferable) {
        if (!ttsServiceRegistered) {
            return
        }
        try {
            Thread.sleep(200)
            val transferable = clipboard.getContents(this)
            val what = transferable.getTransferData(DataFlavor.stringFlavor) as String
            _last = what
            _clipboardHistory.tryEmit(what)
            clipboard.setContents(transferable, this)
            failedClipboardCnt = 0
        } catch (e: Exception) {
            e.printStackTrace()
            failedClipboardCnt += 1
            if (failedClipboardCnt < 5) {
                lostOwnership(clipboard, contents)
            } else {
                failedClipboardCnt = 0
            }
        }
    }
}

data class AppState(val clipboardTTS: Boolean = false, val showPanel: Boolean = true)