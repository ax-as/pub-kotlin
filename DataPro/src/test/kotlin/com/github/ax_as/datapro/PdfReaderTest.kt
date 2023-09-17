package com.github.ax_as.datapro

import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class PdfReaderTest {

    val reader = PdfReader()

    @Test @Ignore
    fun extractToText() {

        val t = reader.extractToText("files/trt12123_edital_de_abertura_completo_retificado.pdf")
        print(t)
    }


}