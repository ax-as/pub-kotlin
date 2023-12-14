package com.github.ax_as.datapro

import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ax_as.datapro.cmd.*
import kotlin.system.exitProcess


//fun main(args: Array<String>) = LoadFilesCmd(mutableMapOf()).subcommands(
//    SearchCmd(),
//    JoinCmd(),
//    GroupByCmd(),
//    PrintCmd(),
//).main(args)


fun main(args: Array<String>) {

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

class Main : CliktCommand() {
    init {
        java.util.logging.Logger.getLogger(
            "org.apache"
        ).setLevel(java.util.logging.Level.SEVERE);

    }


    private val verbose by option("-v").flag(default = false)
    override fun run() {
    }
}



