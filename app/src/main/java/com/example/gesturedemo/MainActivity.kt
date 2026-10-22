package com.example.gesturedemo

import androidx.compose.foundation.layout.offset // <--- ДОБАВИТЬ ВОТ ЭТОТ
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gesturedemo.ui.theme.GestureDemoTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestureDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Чтобы проверить конкретный пример, раскомментируйте нужную строку в MainScreen
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // ВЫБЕРИТЕ ОДИН ИЗ ПРИМЕРОВ, РАСКОММЕНТИРОВАВ ЕГО:

    // 3. Распознавание кликов
    ClickDemo(modifier)

    // 4. Обнаружение нажатий с PointerInputScope
    TapPressDemo(modifier)

    // 5. Перетаскивание (только по горизонтали)
    DragDemo(modifier)

    // 6. Перетаскивание (во все стороны)
    PointerInputDrag(modifier)

    // 7. Прокрутка (модификатор scrollable)
    ScrollableModifier(modifier)

    // 8. Прокрутка (вертикальная + горизонтальная)
    ScrollModifiers(modifier)

    // 9, 10, 11. Мультитач: масштабирование, вращение, перемещение
    MultiTouchDemo(modifier)
}

// =========================================================
// 3. РАСПОЗНАВАНИЕ КЛИКОВ (clickable)
// =========================================================
@Composable
fun ClickDemo(modifier: Modifier = Modifier) {
    var colorState by remember { mutableStateOf(true) }
    var bgColor by remember { mutableStateOf(Color.Blue) }

    val clickHandler = {
        colorState = !colorState
        bgColor = if (colorState) Color.Blue else Color.DarkGray
    }

    Box(
        modifier = modifier
            .clickable { clickHandler() }
            .background(bgColor)
            .size(100.dp)
    )
}

// =========================================================
// 4. НАЖАТИЯ С POINTERINPUTSCOPE (detectTapGestures)
// =========================================================
@Composable
fun TapPressDemo(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("Waiting ") }

    val tapHandler = { status: String -> textState = status }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .padding(10.dp)
                .background(Color.Blue)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { tapHandler("onPress Detected") },
                        onDoubleTap = { tapHandler("onDoubleTap Detected") },
                        onLongPress = { tapHandler("onLongPress Detected") },
                        onTap = { tapHandler("onTap Detected") }
                    )
                }
        )
        Spacer(Modifier.height(10.dp))
        Text(textState)
    }
}

// =========================================================
// 5. ПЕРЕТАСКИВАНИЕ (только по горизонтали)
// =========================================================
@Composable
fun DragDemo(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var xOffset by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .offset { IntOffset(xOffset.roundToInt(), 0) }
                .size(100.dp)
                .background(Color.Blue)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { distance ->
                        xOffset += distance
                    }
                )
        )
    }
}

// =========================================================
// 6. ПЕРЕТАСКИВАНИЕ В ЛЮБОМ НАПРАВЛЕНИИ (detectDragGestures)
// =========================================================
@Composable
fun PointerInputDrag(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var xOffset by remember { mutableStateOf(0f) }
        var yOffset by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(xOffset.roundToInt(), yOffset.roundToInt()) }
                .background(Color.Blue)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, distance ->
                        xOffset += distance.x
                        yOffset += distance.y
                    }
                }
        )
    }
}

// =========================================================
// 7. ПРОКРУТКА (модификатор scrollable)
// =========================================================
@Composable
fun ScrollableModifier(modifier: Modifier = Modifier) {
    var offset by remember { mutableStateOf(0f) }
    Box(
        modifier
            .fillMaxSize()
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { distance ->
                    offset += distance
                    distance
                }
            )
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset { IntOffset(0, offset.roundToInt()) }
                .background(Color.Red)
        )
    }
}

// =========================================================
// 8. ПРОКРУТКА (verticalScroll + horizontalScroll)
// =========================================================
@Composable
fun ScrollModifiers(modifier: Modifier = Modifier) {
    val image = ImageBitmap.imageResource(id = com.example.gesturedemo.R.drawable.vacation)

    Box(
        modifier = modifier
            .size(150.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier.size(360.dp, 270.dp)
        ) {
            drawImage(
                image = image,
                topLeft = Offset(x = 0f, y = 0f)
            )
        }
    }
}

// =========================================================
// 9, 10, 11. МУЛЬТИТАЧ (Масштабирование, вращение, перемещение)
// =========================================================
@Composable
fun MultiTouchDemo(modifier: Modifier = Modifier) {
    var scale by remember { mutableStateOf(1f) }
    var angle by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { scaleChange, offsetChange, rotationChange ->
        scale *= scaleChange
        angle += rotationChange
        offset += offsetChange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = angle,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
                .background(Color.Blue)
                .size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GestureDemoTheme {
        MainScreen()
    }
}