package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import com.github.ax_as.datapro.PdfReader
import com.github.ax_as.datapro.TextFilter
import com.github.ax_as.datapro.TextFormatter
import mu.KotlinLogging
import java.nio.file.Path
import kotlin.io.path.pathString

class LoadPdfCmd : CliktCommand(name = "pdf") {
    val logger = KotlinLogging.logger {}

    private val file: Path by argument()
        .path(mustExist = true)
    private val range: Pair<Int, Int>? by option().int().pair()
    private val startMark: String by option(names = arrayOf("-s")).default("")
    private val endMark: String by option(names = arrayOf("-e")).default("")
    private val tabulate: Boolean by option(
        names = arrayOf(
            "--tabulate",
            "-t"
        )
    ).flag(default = false)
    private val replace: List<Pair<String, String>?> by option().pair()
        .multiple()
    private val excludeStart by option(names = arrayOf("-es")).flag()
    private val includeEnd by option(names = arrayOf("-ie")).flag()

    private val skipLines by option().int()
    private val csvSep by option().default(",")


    override fun run() {
        logger.debug { "file: ${file.pathString} pages: $range" }
        val pdfReader = PdfReader()
        val rawText: String
        if (tabulate) {
            rawText = pdfReader.tabulate(file.pathString, sep = csvSep)
            println(rawText)
            return
        } else {
            rawText =
                pdfReader.extractToText(file.pathString, pageRange = range)
//            println(rawText)
        }


        val textFilter = TextFilter()
        val resList =
            textFilter.filter(
                rawText,
                startMark,
                endMark,
                excludeStart = excludeStart,
                includeEnd = includeEnd
            )

        var output = TextFormatter().format(resList)

        for (it in replace) {
            if (it == null) {
                continue
            }
            output = it.first.toRegex(RegexOption.DOT_MATCHES_ALL)
                .replace(output, it.second.replace("\\n", "\n"))
        }

        println(output.trim())
    }

}