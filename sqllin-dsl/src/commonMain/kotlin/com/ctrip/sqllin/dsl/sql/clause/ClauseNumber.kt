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

package com.ctrip.sqllin.dsl.sql.clause

/**
 * The 'WHERE' and 'HAVING' clause properties
 * @author yaqiao
 */

public class ClauseNumber(valueName: String) : ClauseElement(valueName) {

    // Less than, <
    internal infix fun lt(number: Number): SelectCondition = appendNumber("<", number)

    // Less or equal to, <=
    internal infix fun lte(number: Number): SelectCondition = appendNumber("<=", number)

    // Equals, ==
    internal infix fun eq(number: Number?): SelectCondition = appendNullableNumber("=", "IS", number)

    // Not equal to, !=
    internal infix fun neq(number: Number?): SelectCondition = appendNullableNumber("!=", "IS NOT", number)

    // Greater than, >
    internal infix fun gt(number: Number): SelectCondition = appendNumber(">", number)

    // Greater than or equal to, >=
    internal infix fun gte(number: Number): SelectCondition = appendNumber(">=", number)

    internal infix fun inIterable(numbers: Iterable<Number>): SelectCondition {
        val iterator = numbers.iterator()
        require(iterator.hasNext()) { "Param 'numbers' must not be empty!!!" }
        val sql = buildString {
            append(valueName)
            append(" IN (")
            do {
                append(iterator.next())
                val hasNext = iterator.hasNext()
                val symbol = if (hasNext) ',' else ')'
                append(symbol)
            } while (hasNext)
        }
        return SelectCondition(sql)
    }

    internal infix fun between(range: LongRange): SelectCondition {
        val sql = buildString {
            append(valueName)
            append(" BETWEEN ")
            append(range.first)
            append(" AND ")
            append(range.last)
        }
        return SelectCondition(sql)
    }

    private fun appendNumber(symbol: String, number: Number): SelectCondition {
        val sql = buildString {
            append(valueName)
            append(' ')
            append(symbol)
            append(' ')
            append(number)
        }
        return SelectCondition(sql)
    }

    private fun appendNullableNumber(notNullSymbol: String, nullSymbol: String, number: Number?): SelectCondition {
        val sql = buildString {
            append(valueName)
            append(' ')
            append(if (number == null) nullSymbol else notNullSymbol)
            append(' ')
            append(number ?: "NULL")
        }
        return SelectCondition(sql)
    }

    override fun hashCode(): Int = valueName.hashCode()
    override fun equals(other: Any?): Boolean = (other as? ClauseNumber)?.valueName == valueName
}