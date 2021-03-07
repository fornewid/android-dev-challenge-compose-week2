/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.androiddevchallenge.timer.ui.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch
import soup.androiddevchallenge.timer.R
import soup.androiddevchallenge.timer.data.model.CountdownTime
import soup.androiddevchallenge.timer.ui.setup.AnimatedFloatingActionButton
import soup.androiddevchallenge.timer.ui.setup.TimerText
import soup.androiddevchallenge.timer.ui.util.isLandscape
import kotlin.math.roundToInt

@Composable
fun TimerScreen(
    countdownTime: CountdownTime,
    modifier: Modifier = Modifier,
    onStopTimer: () -> Unit = {}
) {
    val animationState = remember { mutableStateOf(countdownTime.second.toFloat()) }
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(
                durationMillis = countdownTime.second * 1000,
                easing = LinearEasing
            ),
            typeConverter = Float.VectorConverter,
            initialValue = countdownTime.second.toFloat(),
            targetValue = 0f
        )
    }
    var playTime by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()
    scope.launch {
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            val animationValue = anim.getValueFromNanos(playTime)
            animationState.value = animationValue
        } while (animationValue > 0f)
    }

    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.isLandscape()) {
        36.dp
    } else {
        24.dp
    }
    BoxWithConstraints {
        ConstraintLayout(
            constraintSet = constraints(),
            modifier = modifier.padding(horizontal = horizontalPadding)
        ) {
            CircularProgressIndicator(
                progress = animationState.value / countdownTime.second,
                modifier = Modifier
                    .layoutId(R.id.progress)
                    .aspectRatio(1f)
            )
            TimerText(
                text = animationState.value.roundToInt().toTimeString(),
                modifier = Modifier.layoutId(R.id.time_text)
            )
            AnimatedFloatingActionButton(
                onClick = { onStopTimer() },
                modifier = Modifier.layoutId(R.id.stop_button)
            ) {
                Icon(
                    Icons.Outlined.Stop,
                    contentDescription = "Stop timer"
                )
            }
        }
    }
}

private fun constraints(): ConstraintSet {
    return ConstraintSet {
        val progress = createRefFor(R.id.progress)
        val timeText = createRefFor(R.id.time_text)
        val stopButton = createRefFor(R.id.stop_button)

        constrain(progress) {
            linkTo(
                start = parent.start,
                top = parent.top,
                end = parent.end,
                bottom = stopButton.top,
                verticalBias = 0.5f,
                horizontalBias = 0.5f,
            )
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
        constrain(timeText) {
            linkTo(
                start = parent.start,
                top = parent.top,
                end = parent.end,
                bottom = stopButton.top
            )
        }
        constrain(stopButton) {
            linkTo(
                start = parent.start,
                end = parent.end
            )
            bottom.linkTo(parent.bottom)
        }
    }
}

private fun Int.toTimeString(): String {
    val hour: Int = this / 3600
    val minute: Int = (this % 3600) / 60
    val second: Int = (this % 3600) % 60
    return when {
        hour > 0 -> "$hour:${minute.toUnitTimeString()}:${second.toUnitTimeString()}"
        minute > 0 -> "$minute:${second.toUnitTimeString()}"
        else -> "$second"
    }
}

private fun Int.toUnitTimeString(): String {
    return toString().padStart(2, '0')
}
