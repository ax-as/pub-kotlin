package com.github.ax_as.dataserpro.data

import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.size
import java.nio.file.Path
import java.text.Normalizer
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class Data(name: String, frame: DataFrame<*>) {
    private val frame: DataFrame<*>
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

        fun fromCsv(
            path: Path,
            locale: Locale,
            delimiter: Char
        ): Data {
            val frame = DataFrame.readCSV(
                path.absolutePathString(), parserOptions = ParserOptions(
                    locale = locale,
                ), delimiter = delimiter
            )

            return Data(path.name, frame)
        }

        fun fromCsv(
            filepaths: List<Path>,
            locale: Locale,
            delimiter: Char
        ): List<Data> {
            val frames = mutableListOf<Data>()
            for (filepath in filepaths) {
                frames.add(fromCsv(filepath, locale, delimiter))
            }
            return frames

        }
    }

    fun search(col: String, arg: String): Data {
        val columnName = findColumn(col)

        if (columnName.isEmpty()) {
            return Data(name, DataFrame.Empty)
        }

        val rows = frame.filter {
            it[columnName].toString().lowercase()
                .contains(arg.lowercase())
        }

        if (rows.size().nrow == 0) {
            return Data(name, DataFrame.Empty)
        }

        return Data(name, rows)
    }

    private fun findColumn(col: String): String {
        val k = cols.keys.find { it.contains(col) }.orEmpty()
        return cols[k] ?: ""
    }


    fun groupBy(col: String, into: String): Data {
        val column = findColumn(col)
        if (column.isEmpty()) {
            return this
        }
        val gb = frame.groupBy(column).sortByCount().into(into)

        return Data(name, gb)
    }

    fun print(filter: List<String> = listOf()) {
        if (filter.isEmpty()) {
            frame.print(rowsLimit = 50, valueLimit = 50)
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

        val d = frame.select(*selectedColumns.toTypedArray())
        d.print()
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


}
