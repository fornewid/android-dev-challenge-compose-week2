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
package soup.androiddevchallenge.timer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import soup.androiddevchallenge.timer.data.model.CountdownTime
import soup.androiddevchallenge.timer.ui.animation.FadeThrough
import soup.androiddevchallenge.timer.ui.setup.SetupScreen
import soup.androiddevchallenge.timer.ui.timer.TimerScreen
import soup.androiddevchallenge.timer.ui.util.BackPressHandler

sealed class Destination {
    object Setup : Destination()
    class Timer(val countdownTime: CountdownTime) : Destination()
}

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val destination = rememberDestination()
    if (destination.value is Destination.Timer) {
        BackPressHandler {
            destination.value = Destination.Setup
        }
    }
    FadeThrough(
        targetState = destination.value,
        modifier = modifier
    ) { current ->
        when (current) {
            Destination.Setup -> {
                SetupScreen(
                    modifier = modifier,
                    onStartTimer = { countdownTime ->
                        destination.value = Destination.Timer(countdownTime)
                    }
                )
            }
            is Destination.Timer -> {
                TimerScreen(
                    countdownTime = current.countdownTime,
                    modifier = modifier,
                    onStopTimer = {
                        destination.value = Destination.Setup
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberDestination(
    initialValue: Destination = Destination.Setup
): MutableState<Destination> = remember {
    mutableStateOf(initialValue)
}
