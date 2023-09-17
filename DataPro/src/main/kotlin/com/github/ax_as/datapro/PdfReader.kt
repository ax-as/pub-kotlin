package com.github.ax_as.datapro

import com.github.ax_as.datapro.cmd.CustomPdfStripper
import mu.KotlinLogging
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.fit.pdfdom.PDFDomTree
import technology.tabula.ObjectExtractor
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm
import java.io.File
import java.io.StringWriter
import kotlin.math.min

class PdfReader {
    private val logger = KotlinLogging.logger {}

    fun extractToHtml(
        filepath: String,
        pageRange: Pair<Int, Int>? = null,
        clear: Boolean = true
    ): String {

        val pdfFile = File(filepath)
        var html: String = ""
        PDDocument.load(pdfFile).use {
            val output = StringWriter()
            val parser = PDFDomTree()
            parser.writeText(it, output)
            output.close()
            html = output.toString()
        }
        return html

    }

    fun extractToText(
        filepath: String,
        pageRange: Pair<Int, Int>? = null,
        htmlTags: Boolean = true,
        clear: Boolean = true
    ): String {
        val pdfFile = File(filepath)
        val text: String
        PDDocument.load(pdfFile).use {
            val stripper =
                if (htmlTags) CustomPdfStripper() else PDFTextStripper()

            val (start, end) = getRange(pageRange, it)

            text = buildString {
                for (i in start..end) {
                    stripper.startPage = i
                    stripper.endPage = i
                    append(stripper.getText(it))
                }
            }
        }

        if (clear) {
            return clearText(text)
        }

        return text
    }


    private fun clearText(rawText: String): String {
        val lineWithOnlyNumber = Regex(
            "(?<=^|\\n)\\s*(\\d+)\\s*(?=\$|\\n)\n",
            setOf(RegexOption.MULTILINE)
        )

        var t = rawText.replace(lineWithOnlyNumber, "").lines()
            .filter {
                it.isNotBlank()
            }.joinToString("\n").replace(lineWithOnlyNumber, "")

//        return joinBrokenWords(t)

        return t

    }

    private fun getRange(
        pageRange: Pair<Int, Int>?,
        it: PDDocument
    ) = when (pageRange) {
        null -> Pair(1, it.numberOfPages)
        else -> Pair(
            pageRange.first,
            min(pageRange.second, it.numberOfPages)

        )
    }

    fun tabulate(filepath: String): String {
        val pdfFile = File(filepath)
        val sb = StringBuilder()
        PDDocument.load(pdfFile).use {
            val sea = SpreadsheetExtractionAlgorithm()
            val pi = ObjectExtractor(it).extract()

            while (pi.hasNext()) {
                val page = pi.next()
                val tables = sea.extract(page)
                for (table in tables) {

                    val rows = table.rows
                    for (cell in rows) {

                        for (content in cell) {
                            val text: String =
                                content.getText().replace("\n", " ")
                                    .replace("\r", " ")
                            sb.append("$text,")
                            logger.debug { text }
                        }
                        sb.appendLine()
                        logger.debug { "" }
                    }
                }
            }
        }


        return sb.toString()
    }


    fun joinBrokenWords(text: String): String {
        val lines = text.split("\n")
        val sb = StringBuilder()

        for (i in lines.indices) {
            var line = lines[i]

            // Se a linha não termina com pontuação ou espaço, provavelmente está quebrada
            if (line.isNotEmpty() && !line.last()
                    .isWhitespace() && !".!?,".contains(line.last())
            ) {
                // Junta a linha atual com a próxima linha, se houver
                if (i + 1 < lines.size) {
                    line += lines[i + 1].split(" ").first()
                }
            }

            sb.append(line).append("\n")
        }

        return sb.toString()
    }


}
