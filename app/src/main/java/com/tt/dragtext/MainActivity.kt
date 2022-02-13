package com.tt.dragtext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.tt.dragtext.ui.theme.DragTextTheme
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DragTextTheme(false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("TOMOSIA Mobile Team")
                }
            }
        }
    }
}

@Composable
fun Greeting(animatedString: String) {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val rnd = Random()

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        animatedString.mapIndexed { index, c ->
            val dragOffsetChildren = animateOffsetAsState(
                targetValue = offset.value,
                tween(37 + index * 30, easing = LinearOutSlowInEasing)
            )

            val color = Color(
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256)
            ).copy(alpha = 0.9f)

            if (index == 0) {
                Text(modifier = Modifier
                    .offset {
                        IntOffset(
                            offset.value.x.roundToInt(),
                            offset.value.y.roundToInt()
                        )
                    }
                    .pointerInput(Unit) {
                        scope.launch {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    val original = offset.value
                                    val summed = original + dragAmount
                                    scope.launch {
                                        offset.snapTo(summed)
                                    }
                                }
                            )
                        }
                    },
                    color = color,
                    text = "$c",
                    fontSize = 27.sp
                )
                return@mapIndexed
            }

            Text(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            dragOffsetChildren.value.x.roundToInt(),
                            dragOffsetChildren.value.y.roundToInt()
                        )
                    },
                text = "$c",
                color = color,
                fontSize = 27.sp
            )
        }
    }
}
