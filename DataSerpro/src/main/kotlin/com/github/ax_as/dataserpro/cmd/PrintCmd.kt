package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ax_as.dataserpro.data.Data
import mu.KotlinLogging

class PrintCmd() : CliktCommand(name = "print") {
    private val colFilter by option("--cols").split(",")
    private val ctx by requireObject<MutableMap<String, Any>>()
    private val logger = KotlinLogging.logger {}
    override fun run() {
        logger.debug { "colFilter: $colFilter" }
        val results = ctx["data-sets"] as List<*>

        results.forEach {
            val r = it as Data

            r.print(filter = colFilter ?: listOf())
        }
    }
}