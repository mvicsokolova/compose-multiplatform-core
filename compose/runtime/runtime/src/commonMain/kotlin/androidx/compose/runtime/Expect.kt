/*
 * Copyright 2019 The Android Open Source Project
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

package androidx.compose.runtime

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotContextElement
import kotlinx.coroutines.CancellationException

/**
 * Returns the hash code for the given object that is unique across all currently allocated objects.
 * The hash code for the null reference is zero.
 *
 * Can be negative, and near Int.MAX_VALUE, so it can overflow if used as part of calculations.
 * For example, don't use this:
 * ```
 * val comparison = identityHashCode(midVal) - identityHashCode(leftVal)
 * if (comparison < 0) ...
 * ```
 * Use this instead:
 * ```
 * if (identityHashCode(midVal) < identityHashCode(leftVal)) ...
 * ```
 */
@InternalComposeApi
expect fun identityHashCode(instance: Any?): Int

internal expect class AtomicReference<V>(value: V) {
    fun get(): V
    fun set(value: V)
    fun getAndSet(value: V): V
    fun compareAndSet(expect: V, newValue: V): Boolean
}

internal expect class AtomicInt(value: Int) {
    fun get(): Int
    fun set(value: Int)
    fun add(amount: Int): Int
    fun compareAndSet(expect: Int, newValue: Int): Boolean
    fun getAndSet(value: Int): Int
}

internal fun AtomicInt.postIncrement(): Int = add(1) - 1

expect annotation class CompositionContextLocal

internal expect class WeakReference<T : Any>(reference: T) {
    fun get(): T?
}

@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
expect annotation class TestOnly()

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
expect annotation class CheckResult(
    val suggest: String
)

/**
 * The [MonotonicFrameClock] used by [withFrameNanos] and [withFrameMillis] if one is not present
 * in the calling [kotlin.coroutines.CoroutineContext].
 *
 * This value is no longer used by compose runtime.
 */
@Deprecated(
    "MonotonicFrameClocks are not globally applicable across platforms. " +
        "Use an appropriate local clock."
)
expect val DefaultMonotonicFrameClock: MonotonicFrameClock

internal expect fun invokeComposable(composer: Composer, composable: @Composable () -> Unit)

internal expect fun <T> invokeComposableForResult(
    composer: Composer,
    composable: @Composable () -> T
): T

@OptIn(ExperimentalComposeApi::class)
internal expect class SnapshotContextElementImpl(
    snapshot: Snapshot
) : SnapshotContextElement

internal expect fun logError(message: String, e: Throwable)

internal expect fun currentThreadId(): Long

internal expect fun currentThreadName(): String

/**
 * Represents a platform-optimized cancellation exception.
 * This allows us to configure exceptions separately on JVM and other platforms.
 */
internal expect abstract class PlatformOptimizedCancellationException(
    message: String? = null
) : CancellationException
