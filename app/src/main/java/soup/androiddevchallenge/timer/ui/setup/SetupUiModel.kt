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

import soup.androiddevchallenge.timer.data.model.CountdownTime

data class SetupUiModel(
    val time: String = "00h 00m 00s",
    val canStart: Boolean = false,
    val canDelete: Boolean = false
)

sealed class SetupUiEvent {
    class StartTimer(val countdownTime: CountdownTime) : SetupUiEvent()
}

enum class NumberPad(val number: Int) {
    ONE(1), TWO(2), THREE(3),
    FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9),
    ZERO(0), SPACE(-1)
}
