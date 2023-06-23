package goods

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlin.math.pow
import kotlin.random.Random


data class Point(var x: Float, var y: Float, var dx: Float, var dy: Float, var effectDistance: Int, val color: Color) {
    operator fun minus(other: Point): Float {
        return (x - other.x).pow(2) + (y - other.y).pow(2)
    }
}

fun createPoints(color: Color, count: Int, size: IntSize): Array<Point> {
    return Array(count) {
        Point(
            Random.nextInt(0, size.width).toFloat(),
            Random.nextInt(0, size.height).toFloat(),
            2 * Random.nextFloat() - 1,
            2 * Random.nextFloat() - 1,
            6000, color
        )
    }
}


fun Point.isValid(size: Size): Boolean {
    return x >= 0 && x <= size.width && y >= 0 && y <= size.height
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawNest() {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }
    var points: Array<Point> by remember { mutableStateOf(emptyArray()) }
    val mousePoint: Point = remember { Point(Float.NaN, Float.NaN, 0f, 0f, 20000, Color.Red) }
    Canvas(modifier = Modifier.fillMaxSize()
        .onPointerEvent(PointerEventType.Move) {
            val position = it.changes.first().position
            mousePoint.x = position.x
            mousePoint.y = position.y
        }.onPointerEvent(PointerEventType.Exit){
            mousePoint.x = Float.NaN
            mousePoint.y = Float.NaN
        }.onSizeChanged {
            points = createPoints(Color.Red, 100, it) + arrayOf(mousePoint)
        }.graphicsLayer {
            time
            renderEffect
        }) {
        val (width, height) = size
        points.forEachIndexed { index, point ->
            point.x += point.dx
            point.y += point.dy
            if (point.x < 0 || point.x > width) {
                point.dx *= -1
            }
            if (point.y < 0 || point.y > height) {
                point.dy *= -1
            }
            if (!point.isValid(size)) {
                return@forEachIndexed
            }
            drawRect(point.color, Offset(point.x, point.y), Size(1f, 1f))
            // draw line
            for (i in (index + 1) until points.size) {
                val target = points[i]
                if (!target.isValid(size)) {
                    continue
                }
                val distance = point - target

                if (distance < target.effectDistance) {
                    if (target == mousePoint) {
                        point.x -= 0.02f * (point.x - target.x)
                        point.y -= 0.02f * (point.y - target.y)
                    }
                    drawLine(
                        Color.Red,
                        Offset(point.x, point.y),
                        Offset(target.x, target.y),
                        (target.effectDistance - distance) / (2 * target.effectDistance)
                    )
                }
            }

        }
    }
}