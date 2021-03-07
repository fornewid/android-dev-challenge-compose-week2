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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import soup.androiddevchallenge.timer.R
import soup.androiddevchallenge.timer.data.model.CountdownTime
import soup.androiddevchallenge.timer.event.EventObserver
import soup.androiddevchallenge.timer.ui.util.isLandscape

@Composable
fun SetupScreen(
    modifier: Modifier = Modifier,
    onStartTimer: (CountdownTime) -> Unit = {}
) {
    val viewModel: SetupViewModel = viewModel()
    val uiModel by viewModel.uiModel.observeAsState(SetupUiModel())
    val uiEvent by viewModel.uiEvent.observeAsState()
    EventObserver(uiEvent) { event ->
        when (event) {
            is SetupUiEvent.StartTimer -> {
                onStartTimer(event.countdownTime)
            }
        }
    }

    BoxWithConstraints {
        val configuration = LocalConfiguration.current
        val (constraints, horizontalPadding) = if (configuration.isLandscape()) {
            landscapeConstraints() to 36.dp
        } else {
            portraitConstraints() to 24.dp
        }
        ConstraintLayout(
            constraintSet = constraints,
            modifier = modifier.padding(horizontal = horizontalPadding)
        ) {
            TimerText(
                text = uiModel.time,
                modifier = Modifier.layoutId(R.id.time_text),
                enabled = uiModel.canStart
            )
            BackspaceButton(
                onClick = { viewModel.onBackspaceClick() },
                onLongClick = { viewModel.onBackspaceLongClick() },
                modifier = Modifier.layoutId(R.id.backspace_button),
                enabled = uiModel.canDelete
            )
            Divider(modifier = Modifier.layoutId(R.id.divider))
            Spacer(
                modifier = Modifier
                    .layoutId(R.id.guideline)
                    .width(80.dp)
            )
            NumberPad(
                modifier = Modifier.layoutId(R.id.number_pad),
                onNumberPadClick = { viewModel.onNumberPadClick(it) }
            )
            AnimatedFloatingActionButton(
                onClick = { viewModel.onStartClick() },
                modifier = Modifier.layoutId(R.id.start_button),
                visible = uiModel.canStart
            ) {
                Icon(
                    Icons.Outlined.PlayArrow,
                    contentDescription = "Start timer"
                )
            }
        }
    }
}

private fun portraitConstraints(): ConstraintSet {
    return ConstraintSet {
        val timeText = createRefFor(R.id.time_text)
        val backspaceButton = createRefFor(R.id.backspace_button)
        val divider = createRefFor(R.id.divider)
        val guideline = createRefFor(R.id.guideline)
        val numberPad = createRefFor(R.id.number_pad)
        val startButton = createRefFor(R.id.start_button)

        constrain(timeText) {
            linkTo(
                start = parent.start,
                top = parent.top,
                end = backspaceButton.start,
                bottom = guideline.top
            )
            width = Dimension.fillToConstraints
        }
        constrain(backspaceButton) {
            end.linkTo(parent.end)
            linkTo(
                top = timeText.top,
                bottom = timeText.bottom
            )
        }
        constrain(divider) {
            bottom.linkTo(guideline.bottom)
        }
        constrain(guideline) {
            top.linkTo(parent.top, margin = 160.dp)
        }
        constrain(numberPad) {
            linkTo(
                top = guideline.bottom,
                topMargin = 16.dp,
                bottom = parent.bottom,
                bottomMargin = 80.dp,
                bias = 0f
            )
        }
        constrain(startButton) {
            linkTo(
                start = parent.start,
                end = parent.end
            )
            bottom.linkTo(parent.bottom)
        }
    }
}

private fun landscapeConstraints(): ConstraintSet {
    return ConstraintSet {
        val timeText = createRefFor(R.id.time_text)
        val backspaceButton = createRefFor(R.id.backspace_button)
        val divider = createRefFor(R.id.divider)
        val guideline = createRefFor(R.id.guideline)
        val numberPad = createRefFor(R.id.number_pad)
        val startButton = createRefFor(R.id.start_button)

        constrain(timeText) {
            linkTo(
                start = parent.start,
                top = parent.top,
                end = backspaceButton.start,
                bottom = guideline.top
            )
            width = Dimension.fillToConstraints
        }
        constrain(backspaceButton) {
            end.linkTo(numberPad.start, margin = 8.dp)
            linkTo(
                top = timeText.top,
                bottom = timeText.bottom
            )
        }
        constrain(divider) {
            linkTo(
                start = parent.start,
                end = backspaceButton.end
            )
            bottom.linkTo(timeText.bottom)
            width = Dimension.fillToConstraints
        }
        constrain(guideline) {
            linkTo(
                start = parent.start,
                end = parent.end
            )
            bottom.linkTo(parent.bottom, margin = 80.dp)
        }
        constrain(numberPad) {
            linkTo(
                start = guideline.end,
                startMargin = 16.dp,
                end = parent.end,
                top = parent.top,
                topMargin = 8.dp,
                bottom = parent.bottom
            )
            width = Dimension.fillToConstraints
        }
        constrain(startButton) {
            linkTo(
                start = parent.start,
                end = parent.end
            )
            bottom.linkTo(parent.bottom)
        }
    }
}
