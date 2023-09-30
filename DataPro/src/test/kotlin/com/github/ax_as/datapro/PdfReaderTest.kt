package com.github.ax_as.datapro

import org.junit.jupiter.api.Test

class PdfReaderTest {

    val reader = PdfReader()

    @Test
    fun extractToText() {

        val t = reader.tabulate("/data/files/bb_final.pdf")
        print(t)
    }


}