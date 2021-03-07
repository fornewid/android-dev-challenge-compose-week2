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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.androiddevchallenge.timer.data.model.CountdownTime
import soup.androiddevchallenge.timer.event.EventLiveData
import soup.androiddevchallenge.timer.event.MutableEventLiveData

class SetupViewModel : ViewModel() {

    private val _uiModel = MutableLiveData(SetupUiModel())
    val uiModel: LiveData<SetupUiModel>
        get() = _uiModel

    private val _uiEvent = MutableEventLiveData<SetupUiEvent>()
    val uiEvent: EventLiveData<SetupUiEvent>
        get() = _uiEvent

    private val queue = ArrayDeque<NumberPad>()

    fun onBackspaceClick() {
        queue.removeFirstOrNull()?.also {
            updateUiModelAsync()
        }
    }

    fun onBackspaceLongClick() {
        if (queue.isNotEmpty()) {
            queue.clear()
            updateUiModelAsync()
        }
    }

    fun onNumberPadClick(pad: NumberPad) {
        if (queue.size >= 6 || queue.isEmpty() && pad == NumberPad.ZERO) {
            return
        }
        queue.addFirst(pad)
        updateUiModelAsync()
    }

    fun onStartClick() {
        viewModelScope.launch {
            _uiEvent.event = withContext(Dispatchers.Default) {
                val hour = 10 * getNumberAt(5) + getNumberAt(4)
                val minute = 10 * getNumberAt(3) + getNumberAt(2)
                val second = 10 * getNumberAt(1) + getNumberAt(0)
                SetupUiEvent.StartTimer(CountdownTime.of(hour, minute, second))
            }
        }
    }

    private fun updateUiModelAsync() {
        viewModelScope.launch {
            _uiModel.value = withContext(Dispatchers.Default) {
                val hour = getNumberStringAt(5) + getNumberStringAt(4)
                val minute = getNumberStringAt(3) + getNumberStringAt(2)
                val second = getNumberStringAt(1) + getNumberStringAt(0)
                SetupUiModel(
                    time = """${hour}h ${minute}m ${second}s""",
                    canStart = queue.isNotEmpty(),
                    canDelete = queue.isNotEmpty()
                )
            }
        }
    }

    private fun getNumberStringAt(index: Int): String {
        return queue.getOrElse(index) { NumberPad.ZERO }.number.toString()
    }

    private fun getNumberAt(index: Int): Int {
        return queue.getOrElse(index) { NumberPad.ZERO }.number
    }
}
