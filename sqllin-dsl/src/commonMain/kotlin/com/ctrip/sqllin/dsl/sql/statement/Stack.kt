/*
 * Copyright (C) 2022 Ctrip.com.
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
package com.ctrip.sqllin.dsl.sql.statement

import kotlin.jvm.Volatile

/**
 * A sample Stack implementation
 * @author yaqiao
 */

internal class Stack<T> {

    @Volatile
    private var topNode: Node<T>? = null

    val isEmpty
        get() = topNode == null

    val isNotEmpty
        get() = !isEmpty

    val top: T?
        get() = topNode?.element

    fun pop(): T? {
        var value: T? = null
        topNode = topNode?.let {
            value = it.element
            val newTopNode = it.next?.apply { pre = null }
            it.next = null
            newTopNode
        }
        return value
    }

    fun push(e: T) {
        val newNode = Node(e)
        if (isEmpty)
            topNode = newNode
        else {
            topNode!!.pre = newNode
            newNode.next = topNode
            topNode = newNode
        }
    }
}