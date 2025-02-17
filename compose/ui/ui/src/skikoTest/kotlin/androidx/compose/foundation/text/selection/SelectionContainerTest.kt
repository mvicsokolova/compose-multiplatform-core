/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.foundation.text.selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerIconService
import androidx.compose.ui.platform.LocalPointerIconService
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests relating to [SelectionContainer].
 *
 * The reason this test is in the `ui` module, even though [SelectionContainer] is in `foundation`,
 * is that [PointerIconService] is needed for some of the tests, and it is internal in `ui`.
 */
@OptIn(ExperimentalTestApi::class)
class SelectionContainerTest {
    @Test
    fun selectionContainerSetsTextPointerIcon() = runComposeUiTest {
        lateinit var pointerIconService: PointerIconService
        setContent {
            pointerIconService = LocalPointerIconService.current!!
            SelectionContainer {
                Column {
                    BasicText(
                        text = "Text text text text",
                        modifier = Modifier.testTag("content")
                    )
                }
            }
        }

        onNodeWithTag("content").performMouseInput {
            moveTo(Offset(1f, 1f))
        }
        assertEquals(PointerIcon.Text, pointerIconService.getIcon())
    }
}