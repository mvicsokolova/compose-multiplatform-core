/*
 * Copyright 2023 The Android Open Source Project
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

internal actual class AtomicReference<V> actual constructor(private var value: V) {
    //private val delegate = atomic(value)
    actual fun get() = value
    actual fun set(value: V) {
        this.value = value
    }
    actual fun getAndSet(value: V) = value
    actual fun compareAndSet(expect: V, newValue: V): Boolean {
        if (value == expect) {
            value = newValue;
            return true
        } else
            return false
    }
}

internal actual class AtomicInt actual constructor(private var value: Int) {
    actual fun get() = value
    actual fun set(value: Int) {
        this.value = value
    }
    actual fun getAndSet(value: Int) = value
    actual fun compareAndSet(expect: Int, newValue: Int): Boolean {
        if (value == expect) {
            value = newValue;
            return true
        } else
            return false
    }

    actual fun add(amount: Int): Int {
        return value + amount
    }
}
