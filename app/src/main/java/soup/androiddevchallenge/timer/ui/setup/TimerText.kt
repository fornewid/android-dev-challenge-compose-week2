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
package soup.androiddevchallenge.timer.ui.setup

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun TimerText(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val transitionData = updateTransitionData(enabled)
    Text(
        text = text,
        modifier = modifier,
        color = transitionData.color,
        fontSize = 40.sp,
        textAlign = TextAlign.Center
    )
}

private class TransitionData(color: State<Color>) {
    val color by color
}

@Composable
private fun updateTransitionData(enabled: Boolean): TransitionData {
    val transition = updateTransition(enabled)
    val color = transition.animateColor {
        if (it) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onBackground
                .copy(alpha = ContentAlpha.disabled)
        }
    }
    return remember(transition) { TransitionData(color) }
}
