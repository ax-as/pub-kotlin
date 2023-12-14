package com.github.ax_as.datapro

import mu.KotlinLogging
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import technology.tabula.ObjectExtractor
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm
import java.io.File
import kotlin.math.min

class PdfReader {
    private val logger = KotlinLogging.logger {}

    fun extractToText(
        filepath: String,
        pageRange: Pair<Int, Int>? = null,
        htmlTags: Boolean = false,
        clear: Boolean = true
    ): String {
        val pdfFile = File(filepath)
        val text: String
        Loader.loadPDF(pdfFile).use { doc ->
            val stripper = PDFTextStripper()

            text = buildString {
                append(stripper.getText(doc))
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

        var t = rawText.replace(lineWithOnlyNumber, " ")
        t = t.replace("""(\n+)""".toRegex(RegexOption.DOT_MATCHES_ALL), " ")
        t = t.replace("""(\n)""".toRegex(RegexOption.DOT_MATCHES_ALL), " ")
        t = t.replace("""(\s+)""".toRegex(RegexOption.DOT_MATCHES_ALL), " ")
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

    fun tabulate(filepath: String, sep: String = ","): String {
        val pdfFile = File(filepath)
        val sb = StringBuilder()
        val stripper = PDFTextStripper()


        Loader.loadPDF(pdfFile).use { doc ->

            val sea = SpreadsheetExtractionAlgorithm()
            val pi = ObjectExtractor(doc).extract()

            while (pi.hasNext()) {
                val page = pi.next()
                val tables = sea.extract(page)
                for (table in tables) {

                    val rows = table.rows
                    for (cell in rows) {
                        sb.append(cell.joinToString(sep) { it.getText() })

                        sb.appendLine()
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

            sb.append(line).append(" ")
        }

        return sb.toString()
    }


}
