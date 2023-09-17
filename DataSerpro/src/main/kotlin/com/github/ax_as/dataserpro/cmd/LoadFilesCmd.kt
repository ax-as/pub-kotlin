package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ax_as.dataserpro.data.Data
import mu.KotlinLogging
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name

class LoadCsvCmd() :
    BaseCmd(
        name = "load", invokeWithoutSubcommand = true,
        allowMultipleSubcommands = true
    ) {
    private val logger = KotlinLogging.logger {}
    private val showColumns by option(
        names = arrayOf(
            "--show-cols",
            "--info"
        )
    ).flag()

//    private val files: Set<Path> by argument()
//        .path(mustExist = true)
//        .multiple(required = true)
//        .unique()

    private val csvs by argument().convert {
        val s = it.split(":")
        if (s.size != 2) {
            throw CliktError("formato inválido para o argumento $it")
        }
        Pair(s.first(), s.last())
    }.convert {
        val files = it.second.split(",").map { f -> Path.of(f) }

        if (files.size > 1 && it.first == "") {
            throw CliktError("com multiplos arquivos para um mesmo data frame é obrigatório fornecer o nome do dataframe, no formato nome:arquivo1,arquivo2... ")
        }

        Pair(it.first, files)
    }.multiple()

    private fun getName(pathStr: String): String {
        return Path.of(pathStr).name
    }

    private val defSep = 'c'
    private val seps by option("--seps", "-S").default(defSep.toString())

    fun sepConvert(c: Char): Char {
        return when (c) {
            'c' -> ','
            'p' -> '|'
            's' -> ' '
            else -> c
        }

    }

    override fun run() {
        val sepList = CharArray(csvs.size).apply {
            forEachIndexed { i, _ ->
                if (seps.length > i) {
                    this[i] = sepConvert(seps[i])
                } else {
                    this[i] = sepConvert(defSep)
                }
            }
        }
        for ((index, csv) in csvs.withIndex()) {
            val delimiter = sepList[index]
            val data = merge(csv.first, csv.second, delimiter)
            put(data)
        }

        if (showColumns) {
            forEach { s, d ->
                println(s)
                val names = d.columnNames.joinToString { "\n\t" }
                println("\t$names")
                d
            }
        }
    }

    private fun merge(name: String, files: List<Path>, delimiter: Char): Data {
        val datas = arrayListOf<Data>()
        for (file in files) {
            datas.add(
                Data.fromCsv(
                    name = name,
                    path = file,
                    locale = Locale.US,
                    delimiter = delimiter,
                )
            )
        }

        return Data.mergeOf(datas)
    }
}