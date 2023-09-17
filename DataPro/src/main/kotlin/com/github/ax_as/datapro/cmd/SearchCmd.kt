package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.parameters.arguments.argument
import mu.KotlinLogging

class SearchCmd() : BaseCmd(name = "search", invokeWithoutSubcommand = true) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.debug { "" }
    }

    private val col: String by argument()
    private val query: String by argument()
    override fun run() {
        forEach { _, data ->
            data.search(col, query)
        }
    }


}