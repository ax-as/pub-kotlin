package com.github.ax_as.dataserpro.cmd

import mu.KotlinLogging
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import java.io.Writer

class CustomPdfStripper : PDFTextStripper() {
    private val logger = KotlinLogging.logger {}

    private var last = false

    override fun writeString(
        str: String?,
        textPositions: MutableList<TextPosition>?
    ) {
        val text = StringBuilder()
        if (textPositions != null) {
            for (it in textPositions) {
                val actual = it.font.name.contains("Bold")
                if (actual && !last) {
                    text.append("<b>")
                }
                if (last && !actual) {
                    text.append("</b>")
                }
                text.append(it.unicode)
                last = actual
            }
        }

        super.writeString(text.toString(), textPositions)
    }


}