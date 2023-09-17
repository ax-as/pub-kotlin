package com.github.ax_as.datapro

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ax_as.datapro.cmd.*
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

//fun main(args: Array<String>) = LoadFilesCmd(mutableMapOf()).subcommands(
//    SearchCmd(),
//    JoinCmd(),
//    GroupByCmd(),
//    PrintCmd(),
//).main(args)


fun main(args: Array<String>) {
    logger.debug { (args.map { it }) }
    try {

        Main().subcommands(
            LoadPdfCmd(),
            LoadFilesCmd(mutableMapOf()).subcommands(
                SearchCmd(),
                JoinCmd(),
                GroupByCmd(),
                PrintCmd(),
            ),
        ).completionOption().main(args)
    } catch (e: CliktError) {
        exitProcess(e.statusCode)
    }

}

fun configureLogBack(verbose: Boolean) {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    rootLogger.level = if (verbose) Level.DEBUG else Level.INFO

    val logger = KotlinLogging.logger {}
    logger.debug { "Mensagem de debug" }
}

class Main : CliktCommand() {
    init {
//        completionOption()
    }

    private val verbose by option("-v").flag(default = false)
    override fun run() {
        configureLogBack(verbose)
    }
}



