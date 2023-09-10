package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.unique
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.github.ax_as.dataserpro.data.Data
import mu.KotlinLogging
import java.nio.file.Path
import java.util.*
import kotlin.math.min

class LoadCsvCmd() :
    CliktCommand(
        name = "load",
        invokeWithoutSubcommand = true,
        allowMultipleSubcommands = true
    ) {
    private val logger = KotlinLogging.logger {}
    private val ctx by findOrSetObject { mutableMapOf<String, Any>() }
    private val showColumns by option(names = arrayOf("--show-cols")).flag()

    private val files: Set<Path> by argument()
        .path(mustExist = true)
        .multiple(required = true)
        .unique()

    private val seps by option("--seps", "-S").default(",")
    override fun run() {
        val sepList = CharArray(files.size)
        for (i in files.indices) {
            sepList[i] = seps[0]
        }

        logger.debug { seps }
        for (i in 0 until min(seps.length, files.size)) {
            sepList[i] = seps[i]
        }

        val dataSets = mutableListOf<Data>()

        for ((index, path) in files.withIndex()) {
            val delimiter = sepList[index]
            logger.debug { "$delimiter $path" }
            val frame = Data.fromCsv(
                path = path,
                locale = Locale.US,
                delimiter
            )
            dataSets.add(frame)
        }

        logger.debug { "showColumns $showColumns" }
        if (showColumns) {
            for (dataSet in dataSets) {
                println("DataSet: ${dataSet.name}")
                val colNames = dataSet.columnNames.joinToString(", ")

                println("\t$colNames")
                println()
            }
        }

        ctx["data-sets"] = dataSets
    }
}