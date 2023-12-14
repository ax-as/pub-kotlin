package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import mu.KotlinLogging

class JoinCmd : BaseCmd(name = "join") {
    val logger = KotlinLogging.logger {}
    val col by argument()
    val target by option(names = arrayOf("--target")).default("j1")
    val normalize by option(names = arrayOf("--normalize", "-n")).flag(default = true)
    override fun run() {
        if (count() != 2) {
            throw Error("join só é possível com 2 datasets")
        }

        var c = dataByIndex(0)
        var c2 = dataByIndex(1)
        if (normalize) {
            c = c.normalize(col)
            c2 = c2.normalize(col)
        }

        val joinData = c.join(target, c2, col)
        logger.debug { "nova tabela criada by join ${joinData.name} associada a chave $target" }

        put(c)
        put(c2)
        put(target, joinData)
    }

}