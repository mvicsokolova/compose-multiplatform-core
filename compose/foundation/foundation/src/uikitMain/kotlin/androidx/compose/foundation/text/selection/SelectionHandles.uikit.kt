/*
 * Copyright 2022 The Android Open Source Project
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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.ResolvedTextDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

/**
 * Clickable padding of handler
 */
private val PADDING = 5.dp

/**
 * Radius of handle circle
 */
private val RADIUS = 6.dp

/**
 * Thickness of handlers vertical line
 */
private val THICKNESS = 2.dp

@Composable
internal actual fun SelectionHandle(
    offsetProvider: OffsetProvider,
    isStartHandle: Boolean,
    direction: ResolvedTextDirection,
    handlesCrossed: Boolean,
    lineHeight: Float,
    modifier: Modifier,
) {
    val density = LocalDensity.current
    val lineHeightDp = with(density) { lineHeight.toDp() }
    val paddingPx = with(density) { PADDING.toPx() }
    val radiusPx = with(density) { RADIUS.toPx() }
    val thicknessPx = with(density) { THICKNESS.toPx() }
    val isLeft = isLeft(isStartHandle, direction, handlesCrossed)

    val handleColor = LocalTextSelectionColors.current.handleColor
    Popup(
        popupPositionProvider = remember(isLeft) {
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    val position = offsetProvider.provide()
                    val y = if (isLeft) {
                        position.y - paddingPx - lineHeight - radiusPx * 2
                    } else {
                        position.y - lineHeight
                    }

                    val positionRounded = IntOffset(position.x.roundToInt(), y.roundToInt())

                    return IntOffset(
                        x = anchorBounds.left + positionRounded.x - popupContentSize.width / 2,
                        y = anchorBounds.top + positionRounded.y
                    )
                }
            }
        },
        properties = PopupProperties(
            clippingEnabled = false,
        ),
    ) {
        Spacer(
            modifier.size(
                width = (PADDING + RADIUS) * 2,
                height = RADIUS * 2 + PADDING + lineHeightDp
            )
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        // vertical line
                        drawRect(
                            color = handleColor,
                            topLeft = Offset(
                                x = paddingPx + radiusPx - thicknessPx / 2,
                                y = if (isLeft) paddingPx + radiusPx else 0f
                            ),
                            size = Size(thicknessPx, lineHeight + radiusPx)
                        )
                        // handle circle
                        drawCircle(
                            color = handleColor,
                            radius = radiusPx,
                            center = center.copy(
                                y = if (isLeft) paddingPx + radiusPx else lineHeight + radiusPx
                            )
                        )
                    }
                }
        )
    }
}

/**
 * Computes whether the handle's appearance should be left-pointing or right-pointing.
 */
private fun isLeft(
    isStartHandle: Boolean,
    direction: ResolvedTextDirection,
    handlesCrossed: Boolean
): Boolean {
    return if (isStartHandle) {
        isHandleLtrDirection(direction, handlesCrossed)
    } else {
        !isHandleLtrDirection(direction, handlesCrossed)
    }
}

/**
 * This method is to check if the selection handles should use the natural Ltr pointing
 * direction.
 * If the context is Ltr and the handles are not crossed, or if the context is Rtl and the handles
 * are crossed, return true.
 *
 * In Ltr context, the start handle should point to the left, and the end handle should point to
 * the right. However, in Rtl context or when handles are crossed, the start handle should point to
 * the right, and the end handle should point to left.
 */
/*@VisibleForTesting*/
private fun isHandleLtrDirection(
    direction: ResolvedTextDirection,
    areHandlesCrossed: Boolean
): Boolean {
    return direction == ResolvedTextDirection.Ltr && !areHandlesCrossed ||
        direction == ResolvedTextDirection.Rtl && areHandlesCrossed
}
