package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.pair
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import com.github.ax_as.dataserpro.PdfReader
import com.github.ax_as.dataserpro.TextFilter
import com.github.ax_as.dataserpro.TextFormatter
import mu.KotlinLogging
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.file.Path
import kotlin.io.path.pathString

class LoadPdfCmd : CliktCommand(name = "pdf") {
    private val logger = KotlinLogging.logger {}
    private val file: Path by argument()
        .path(mustExist = true)
    private val range: Pair<Int, Int>? by option().int().pair()
    private val startMark: String by option(names = arrayOf("-s")).default("")
    private val endMark: String by option(names = arrayOf("-e")).default("")

    override fun run() {
        logger.debug { "file: ${file.pathString} pages: $range" }
        val pdfReader = PdfReader()
        val rawText =
            pdfReader.extractToText(file.pathString, pageRange = range)

        val textFilter = TextFilter()
        val resList =
            textFilter.filter(rawText, startMark, endMark, includeStart = false)
        println(TextFormatter().format(resList.joinToString("\n")))

    }

}