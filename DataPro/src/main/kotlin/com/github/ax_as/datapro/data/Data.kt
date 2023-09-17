package com.github.ax_as.datapro.data

import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.AnyCol
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.DisplayConfiguration
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.toStandaloneHTML
import org.jetbrains.kotlinx.dataframe.size
import java.nio.file.Path
import java.text.Normalizer
import java.util.*
import kotlin.io.path.absolutePathString

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class Data(name: String, frame: DataFrame<*>) {
    private var frame: DataFrame<*>
    val name: String

    init {
        this.name = name
        this.frame = frame.head(Int.MAX_VALUE)
    }

    private val logger = KotlinLogging.logger {}

    val isEmpty: Boolean
        get() = this.frame.size().nrow == 0

    val columnNames: List<String>
        get() = cols.values.toList()

    private val cols: Map<String, String>
        get() = frame.columnNames().associateBy {
            val nKey = Normalizer.normalize(
                it,
                Normalizer.Form.NFD
            ).replace(REGEX_UNACCENT, "")

            val key = nKey.lowercase().replace("[.,\\s]".toRegex(), "")
            key
        }

    companion object {
        fun empty(): Data {
            return Data("", DataFrame.Empty)
        }

        fun fromCsv(
            name: String,
            path: Path,
            locale: Locale,
            delimiter: Char
        ): Data {
            val frame = DataFrame.readCSV(
                path.absolutePathString(), parserOptions = ParserOptions(
                    locale = locale,
                ), delimiter = delimiter
            )

            return Data(name, frame)
        }

        fun mergeOf(datas: List<Data>): Data {

            val d: Data = Data(datas.first().name, datas.first().frame).apply {
                datas.drop(1).forEach { data ->
                    this.frame = this.frame.concat(data.frame)
                }
            }

            return d
        }

        fun concat(name: String, datas: List<Data>): Data {
            val list = datas.map { it.frame }
            val conc = list.concat()

            return Data(name, conc)
        }

    }

    fun search(col: String, arg: String): Data {
        val columnName = findColumn(col) ?: return Data(name, DataFrame.Empty)

        val rows = frame.filter {
            it[columnName].toString().lowercase()
                .contains(arg.lowercase())
        }

        if (rows.size().nrow == 0) {
            return Data(name, DataFrame.Empty)
        }

        return Data(name, rows)
    }

    private fun findColumn(col: String): AnyCol? {
        val k = cols.keys.find { it.contains(col) }.orEmpty()
//        return frame.getColumn(cols[k] ?: "")
        val s = cols[k]
        return frame.getColumnOrNull(s ?: "")
//        return cols[k] ?: ""
    }

    fun gb(col: String, into: String): Data {
        val column = findColumn(col) ?: return this

        val ndf = this.frame.groupBy { column }.aggregate {
            count() into "total"
        }

        return Data(this.name, ndf)

    }

    fun groupBy(col: String, into: String): Data {
        val column = findColumn(col) ?: return this
        val gb = frame.groupBy(column).sortByCount().into(into)

        return Data(name, gb)
    }


    fun join(targetName: String, data2: Data, col: String): Data {
        val column = findColumn((col)) ?: return this

        val df = this.frame.join(data2.frame, JoinType.Inner) { column }
        return Data(
            targetName, df
        )

    }

    fun normalize(col: String): Data {
        val c = this.findColumn(col) ?: return this
        val nd = frame.update(c) { row ->
            row.toString().lowercase().trim().split(" ")
                .joinToString(" ") { sep ->
                    sep.replaceFirstChar { fs ->
                        fs.uppercaseChar()
                    }
                }
        }
        return Data(name, nd)
    }

    //    return tabela1.join(tabela2, JoinType.Inner) { this["Nome"] }.head(
//    Int.MAX_VALUE
//    )
    fun printHtml() {
        frame.toStandaloneHTML().openInBrowser()
    }

    fun print(filter: List<String> = listOf(), html: Boolean = true) {
        logger.debug { "filter $filter" }
        if (filter.isEmpty()) {
            printHtml()
            return
        }

        val selectedColumns = mutableSetOf<String>()
        val allcols = cols
        filter.forEach { fstr ->
            val found = allcols.filter {
                shouldMatch(it, fstr)
            }.map { it.value }
            selectedColumns.addAll(found)
        }
        if (selectedColumns.isEmpty()) {
            return
        }

        val dataFrame = frame.select(*selectedColumns.toTypedArray())
        logger.debug { "toHtml: $name ${dataFrame.columnNames()}" }
//        d.print(rowsLimit = 50, valueLimit = 50)

        if (!html) {
            dataFrame.head(200).print(rowsLimit = 200)
            return
        }
        printHtml(dataFrame)
    }

    private fun printHtml(dataFrame: DataFrame<Any?>) {
        dataFrame.head(Int.MAX_VALUE).toStandaloneHTML(
            DisplayConfiguration(
                rowsLimit = 6000,
                cellContentLimit = 100
            )
        ).openInBrowser()
    }

    private fun shouldMatch(
        it: Map.Entry<String, String>,
        qstr: String
    ): Boolean {
        if (qstr.startsWith("+")) {
            val q = qstr.removePrefix("+").lowercase()
            val lkey = it.key.lowercase()
            val lvalue = it.value.lowercase().trim()
//            logger.debug { "startsWith $q $lkey $lvalue" }
            return q == lkey || q == lvalue
        }

        return it.key.contains(qstr) || it.value.contains(qstr)
    }

    override fun toString(): String {
        return "Data(name='$name')"
    }
}
