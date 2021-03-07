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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NumberPad(
    modifier: Modifier = Modifier,
    onNumberPadClick: (NumberPad) -> Unit = {}
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        modifier = modifier
    ) {
        items(NUMBER_PADS) { item ->
            when (item) {
                NumberPad.SPACE -> Spacer(
                    modifier = Modifier.fillMaxSize()
                )
                else -> TextButton(
                    onClick = { onNumberPadClick(item) },
                    modifier = Modifier.heightIn(min = 60.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = item.number.toString(),
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

private val NUMBER_PADS = listOf(
    NumberPad.ONE, NumberPad.TWO, NumberPad.THREE,
    NumberPad.FOUR, NumberPad.FIVE, NumberPad.SIX,
    NumberPad.SEVEN, NumberPad.EIGHT, NumberPad.NINE,
    NumberPad.SPACE, NumberPad.ZERO, NumberPad.SPACE
)
