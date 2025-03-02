# 高级查询

我们已经学习了基础查询和在条件查询中使用 SQL 函数。现在我们来学习一些更高级的查询技巧。

## Union

_UNION_ 操作符用于合并两个查询结果相同的 _SELECT_ 语句。

在 SQL 中， _UNION_ 操作符位于两个 _SELECT_ 语句中间，但是在 SQLlin 中，我们使用一个高阶函数来实现 _UNION_ ：

```kotlin
fun sample() {
    lateinit var selectStatement: SelectStatement<Person>
    database {
        PersonTable { table ->
            selectStatement = UNION {
                table SELECT WHERE (age GTE 5)
                table SELECT WHERE (length(name) LTE 8)
            }
        }
    }
}
```

你只需要将你的 _SELECT_ 语句写在 `UNION {...}` 块内部。 `UNION {...}`  块内部至少要有两个 _SELECT_
语句，否则你将会在运行时得到一个 `IllegalStateException` 异常。

如果你想要交替使用 _UNION_ 和 _UNION ALL_ ，请使用 `UNION {...}` 或 `UNION_ALL {...}` 块嵌套：

```kotlin
fun sample() {
    lateinit var selectStatement: SelectStatement<Person>
    database {
        PersonTable { table ->
            selectStatement = UNION {
                table SELECT WHERE (age GTE 5)
                UNION_ALL {
                    table SELECT WHERE (length(name) LTE 8)
                    table SELECT WHERE (name EQ "Tom")
                }
            }
        }
    }
}
```

前面的代码等价于：

```roomsql
SELECT * FROM person WHERE age >= 5
UNION
SELECT * FROM person WHERE length(name) <= 5
UNION ALL
SELECT * FROM person WHERE name = "Tom"
```

## 子查询

SQLlin 还不支持子查询，我们将会尽快开发该功能。

## Join

SQLlin 目前支持 join 一个表。

我们需要另外两个 `DBEntity`：

```kotlin
@DBRow("transcript")
@Serializable
data class Transcript(
    val name: String?,
    val math: Int,
    val english: Int,
): DBEntity<Transcript> {
    override fun kSerializer(): KSerializer<Transcript> = serializer()
}

@Serializable
data class Student(
    val name: String?,
    val age: Int?,
    val math: Int,
    val english: Int,
): DBEntity<Student> {
    override fun kSerializer(): KSerializer<Student> = serializer()
}
```

`Transcript` 代表另一张表，`Student` 表示 join 的查询结果的类型（所以 `Student` 不需要被添加 `@DBRow` 注解），它拥有所有 `Person` 和 `Transcript`
所拥有的列名。

### Cross Join

```kotlin
fun joinSample() {
    db {
        PersonTable { table ->
            table SELECT CROSS_JOIN<Student>(TABLE<Transcript>(tableTranscript))
        }
    }
}
```

`CROSS_JOIN` 函数接收一个或多个 `Table` 作为参数。在普通的 _SELECT_ 语句中，该语句的查询结果的类型由 _sqllin-processor_ 生成的
`Table` 决定，但是 _JOIN_ 操作符将会将其改变为指定的类型。在前面的示例中， _CROSS_JOIN_ 将该类型改变为了 `Student`。

### Inner Join

```kotlin
fun joinSample() {
    db {
        PersonTable { table ->
            table SELECT INNER_JOIN<Student>(TABLE<Transcript>(tableTranscript)) USING name
            table SELECT NATURAL_INNER_JOIN<Student>(TABLE<Transcript>(tableTranscript))
        }
    }
}
```

`INNER_JOIN` 与 `CROSS_JOIN` 非常相似，不同之处在于 `INNER_JOIN` 需要连接一个 `USING` 子句。如果一个 _INNER JOIN_ 语句没有
`USING` 子句，那么它是不完整的，但是你的代码仍然可以编译，但它在运行时不会做任何事情。当前，SQLlin 只支持 `USING` 子句而不支持
`ON` 子句，它将会在未来的版本中被支持。

`NATURAL_INNER_JOIN` 将会产生一个完整的 _SELECT_ 语句（与 `CROSS_JOIN` 相似）。所以，你不能再它末尾连接 `USING` 子句，这将由
Kotlin 编译器来保证。

`INNER_JOIN` 拥有一个别名——`JOIN`， `NATURAL_INNER_JOIN` 也拥有一个别名——`NATURAL_JOIN` 。这就像你在 SQL 的 inner join
查询中可以省略 `INNER` 关键字一样。

### Left Outer Join

```kotlin
fun joinSample() {
    db {
        PersonTable { table ->
            table SELECT LEFT_OUTER_JOIN<Student>(TABLE<Transcript>(tableTranscript)) USING name
            table SELECT NATURAL_LEFT_OUTER_JOIN<Student>(TABLE<Transcript>(tableTranscript))
        }
    }
}
```

`LEFT_OUTER_JOIN` 的用法与 `INNER_JOIN` 非常相似，但你使用它的时候要非常小心。由于 SQL 中的 _LEFT OUTER JOIN_
自身的行为，查询结果将产生一些包含空值的行。如果你期待被反序列化的 `DBEntity` 拥有一些非空的属性，在反序列化的时候可能会发生 crash。

## 最后

你已经学习了所有的 SQLlin 用法，享受你的 SQLlin 的编程旅程并对它的更新保持关注吧 :)