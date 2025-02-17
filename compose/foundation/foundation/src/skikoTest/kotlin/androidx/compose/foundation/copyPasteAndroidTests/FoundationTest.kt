/*
 * Copyright 2020 The Android Open Source Project
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

package androidx.compose.foundation.copyPasteAndroidTests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SkikoComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Constant to emulate very big but finite constraints
 */
val BigTestMaxWidth = 5000.dp
val BigTestMaxHeight = 5000.dp

@OptIn(ExperimentalTestApi::class)
fun SkikoComposeUiTest.setContentForSizeAssertions(
    parentMaxWidth: Dp = BigTestMaxWidth,
    parentMaxHeight: Dp = BigTestMaxHeight,
    content: @Composable () -> Unit
): SemanticsNodeInteraction {
    setContent {
        Box {
            Box(
                Modifier.sizeIn(maxWidth = parentMaxWidth, maxHeight = parentMaxHeight)
                    .testTag("containerForSizeAssertion")
            ) {
                content()
            }
        }
    }

    return onNodeWithTag("containerForSizeAssertion")
}
