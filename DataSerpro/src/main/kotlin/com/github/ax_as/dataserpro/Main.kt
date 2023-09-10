package com.github.ax_as.dataserpro

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ax_as.dataserpro.cmd.*
import mu.KotlinLogging
import org.slf4j.LoggerFactory


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    println()
    Main().subcommands(
        LoadPdfCmd(),
        LoadCsvCmd().subcommands(
            SearchCmd(),
            GroupByCmd(),
            PrintCmd(),
        ),
    ).main(args)


}

fun configureLogBack(verbose: Boolean) {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    rootLogger.level = if (verbose) Level.DEBUG else Level.INFO


    val logger = KotlinLogging.logger {}
    logger.debug { "Mensagem de debug" }
}

class Main : CliktCommand() {
    private val verbose by option("-v").flag(default = false)
    override fun run() {
        configureLogBack(verbose)
    }
}



