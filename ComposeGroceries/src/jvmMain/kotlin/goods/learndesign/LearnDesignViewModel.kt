package goods.learndesign

import androidx.compose.runtime.Composable
import goods.learndesign.chapter.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val registeredComposable = listOf(
    Chapter("第2章 亲密性", listOf(
        { Chapter_2_Section_1() },
        { Chapter_2_Section_2() },
        { Chapter_2_Section_3() },
        { Chapter_2_Section_4() },
        { Chapter_2_Section_5() },
        { Chapter_2_Section_6() }
    )),
    Chapter("第3章 对齐", listOf(
        { Chapter_3_Section_1() },
        { Chapter_3_Section_2() },
        { Chapter_3_Section_3() },
        { Chapter_3_Section_4() },
    ))
)

class LearnDesignViewModel {
    private var chapterIndex = 0
    private var sectionIndex = 0
    private val currentChapter: Chapter
        get() = registeredComposable[chapterIndex]
    private val currentSection: @Composable () -> Unit
        get() = currentChapter.sections[sectionIndex]
    private val hasPre: Boolean
        get() {
            return chapterIndex > 0 || sectionIndex > 0
        }
    private val hasNext: Boolean
        get() {
            return chapterIndex < registeredComposable.size - 1 || sectionIndex < currentChapter.sections.size - 1
        }
    private val _uiState = MutableStateFlow(createUiState())
    val uiState: StateFlow<LearnDesignUiState> = _uiState.asStateFlow()
    val chapters = registeredComposable

    fun setChapterIndex(index:Int) {
        chapterIndex = index
        _uiState.value = createUiState()
    }

    fun pre() {
        move(-1)
    }

    fun next() {
        move(1)
    }

    private fun move(value: Int) {
        sectionIndex += value
        if (sectionIndex >= currentChapter.sections.size && hasNext) {
            sectionIndex = 0
            chapterIndex += 1
        } else if (sectionIndex < 0 && hasPre) {
            chapterIndex -= 1
            sectionIndex = currentChapter.sections.size - 1
        }
        sectionIndex = sectionIndex.coerceIn(0, currentChapter.sections.size - 1)
        _uiState.value = createUiState(value)
    }

    private fun createUiState(step: Int = 0): LearnDesignUiState {
        return LearnDesignUiState(
            chapterIndex,
            currentChapter.name,
            currentSection,
            hasNext = hasNext,
            hasPre = hasPre,
            step = step
        )
    }
}

data class Chapter(val name: String, val sections: List<@Composable () -> Unit>)
data class LearnDesignUiState(
    val chapterIndex: Int,
    val chapterName: String,
    val section: @Composable () -> Unit,
    val hasNext: Boolean,
    val hasPre: Boolean,
    val step: Int
)