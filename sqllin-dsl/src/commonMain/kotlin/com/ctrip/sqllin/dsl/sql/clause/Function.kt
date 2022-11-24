package com.ctrip.sqllin.dsl.sql.clause

import com.ctrip.sqllin.dsl.DBEntity
import com.ctrip.sqllin.dsl.sql.Table
import com.ctrip.sqllin.dsl.sql.X

/**
 * SQLite functions
 * @author yaqiao
 */

public fun <T : DBEntity<T>> Table<T>.count(element: ClauseElement): ClauseNumber =
    ClauseNumber("count(${element.valueName})")

public fun <T : DBEntity<T>> Table<T>.count(x: X): ClauseNumber =
    ClauseNumber("count(*)")

public fun <T : DBEntity<T>> Table<T>.max(element: ClauseElement): ClauseNumber =
    ClauseNumber("max(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.min(element: ClauseElement): ClauseNumber =
    ClauseNumber("min(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.avg(element: ClauseElement): ClauseNumber =
    ClauseNumber("avg(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.sum(element: ClauseElement): ClauseNumber =
    ClauseNumber("sum(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.abs(number: ClauseElement): ClauseNumber =
    ClauseNumber("abs(${number.valueName})")

public fun <T: DBEntity<T>> Table<T>.upper(element: ClauseElement): ClauseString =
    ClauseString("upper(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.lower(element: ClauseElement): ClauseString =
    ClauseString("lower(${element.valueName})")

public fun <T: DBEntity<T>> Table<T>.length(element: ClauseElement): ClauseNumber =
    ClauseNumber("length(${element.valueName})")
