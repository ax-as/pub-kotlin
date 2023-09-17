package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.pair
import mu.KotlinLogging

class GroupByCmd : BaseCmd(name = "gb", invokeWithoutSubcommand = true) {
    val logger = KotlinLogging.logger {}


    val groupBy by argument().pair()

    override fun run() {
        val group = groupBy.first
        val into = groupBy.second

        forEach { _, d ->
//            d.groupBy(group, into)
            d.gb(group, into)
        }
    }
}