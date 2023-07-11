package tts

import com.google.cloud.texttospeech.v1.*
import com.google.protobuf.ByteString
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import java.util.prefs.Preferences
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineEvent

class TtsPlayer(private val text: String) {
    private val _state = MutableStateFlow(PlayingState())
    private val id = TtsManager.generateId()
    private var currentState
        get() = _state.value
        set(value) {
            _state.value = value
        }
    val state = _state.asStateFlow()

    fun play() {
        MainScope().launch {
            currentState = currentState.copy(isPlaying = true)
            TtsManager.play(id, text)
            currentState = currentState.copy(isPlaying = false)
        }
    }

    fun release() {
        TtsManager.release(id)
    }
}

data class PlayingState(val isPlaying: Boolean = false)
private const val TTS_LANGUAGE_CODE = "TTS_LANGUAGE_CODE"
private const val TTS_VOICE_NAME = "TTS_VOICE_NAME"
private const val TTS_SSML_GENDER_VALUE = "TTS_SSML_GENDER_VALUE"
object TtsManager {
    private var autoIncreasingId = AtomicInteger(0)
    private val client by lazy {
        TextToSpeechClient.create()
    }
    private val cache = mutableMapOf<String, ByteString>()
    private var supportVoiceList:List<Voice> = emptyList()

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val playerScope = CoroutineScope(newSingleThreadContext("TtsPlayerThread"))
    private val preferences by lazy {
        Preferences.userRoot()
    }
    val currentLanguageCode:String
        get() = preferences.get(TTS_LANGUAGE_CODE, "ja-JP")
    val currentSsmlVoiceGenderValue:Int
        get() = preferences.getInt(TTS_SSML_GENDER_VALUE, SsmlVoiceGender.MALE_VALUE)
    val currentVoiceName:String
        get() = preferences.get(TTS_VOICE_NAME, "ja-JP-Standard-C")

    fun generateId(): Int {
        return autoIncreasingId.incrementAndGet()
    }

    suspend fun play(playerId: Int, text: String) = withContext(playerScope.coroutineContext) {
        playSync(playerId, text)
    }

    suspend fun getSupportVoiceList():List<Voice> = coroutineScope {
        if (supportVoiceList.isNotEmpty()){
            return@coroutineScope supportVoiceList
        }
        withContext(Dispatchers.IO){
            val request = ListVoicesRequest.getDefaultInstance()
            val response = client.listVoices(request)
            response.voicesList
        }
    }

    fun setCurrentVoice(languageCode:String = currentLanguageCode, voiceName:String = currentVoiceName, ssmlGenderValue:Int = currentSsmlVoiceGenderValue){
        cache.clear()
        Preferences.userRoot().apply {
            put(TTS_LANGUAGE_CODE, languageCode)
            put(TTS_VOICE_NAME, voiceName)
            putInt(TTS_SSML_GENDER_VALUE, ssmlGenderValue)
        }
    }

    private fun playSync(playerId: Int, text: String) {
        if (text !in cache) {
            val input = SynthesisInput.newBuilder().setText(text).build()
            val voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(currentLanguageCode)
                    .setSsmlGenderValue(currentSsmlVoiceGenderValue)
                    .setName(currentVoiceName)
                    .build()
            val audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build()
            val response = client.synthesizeSpeech(input, voice, audioConfig)
            cache[text] = response.audioContent
        }
        val audioInputStream = AudioSystem.getAudioInputStream(cache[text]!!.newInput())
        val audioFormat = audioInputStream.format
        val info = DataLine.Info(Clip::class.java, audioFormat)
        val clip = AudioSystem.getLine(info) as Clip
        val countDown = CountDownLatch(1)
        clip.open(audioInputStream)
        clip.start()
        clip.addLineListener {
            if (it.type == LineEvent.Type.STOP) {
                countDown.countDown()
            }
        }
        countDown.await()
        clip.close()
        audioInputStream.close()
    }

    fun release(playerId: Int) {

    }

    fun release() {
        cache.clear()
    }
}