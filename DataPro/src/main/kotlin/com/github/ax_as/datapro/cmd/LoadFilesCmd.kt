package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.parameters.options.*
import mu.KotlinLogging
import java.nio.file.Path

const val defSep = 'c'

class LoadFilesCmd(override val csv: MutableMap<String, MutableSet<String>>) :
    BaseCmd(
        name = "load", invokeWithoutSubcommand = true,
        allowMultipleSubcommands = true
    ), DataHolder, CsvFilesHolder {
    private val logger = KotlinLogging.logger {}

    private val columnInfo by option(
        names = arrayOf(
            "--column-info",
        ),
        help = "mostra as colunas disponíveis nos arquivos e termina a execução"
    ).flag()

    private val seps by option("--seps").default(defSep.toString())

    private val pdf = mutableMapOf<String, MutableSet<String>>()

    private val _files: Map<String, String> by option("--file").convert {
        it.apply {
            if (endsWith("csv")) addFiles(csv, this) else addFiles(pdf, this)
        }
    }.associate()


    override fun run() {
        val sepList = CharArray(csv.size).apply {
            forEachIndexed { i, _ ->
                if (seps.length > i) {
                    this[i] = sepConvert(seps[i])
                } else {
                    this[i] = sepConvert(defSep)
                }
            }
        }

        for ((index, k) in csv.keys.withIndex()) {
            val list = csv[k] ?: arrayListOf()
            val delimiter = sepList[index]
            val data = loadAndMerge(k, list.map { Path.of(it) }, delimiter)
            put(data)
        }

        if (columnInfo) {
            forEach { s, d ->
                println(s)
                val names = d.columnNames.joinToString("\n\t")
                println("\t$names")
                d
            }
        }
    }


}