package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.pair
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.pair
import com.github.ajalt.clikt.parameters.options.required
import com.github.ax_as.dataserpro.data.Data

class GroupByCmd : CliktCommand(name = "gb", invokeWithoutSubcommand = true) {
    val groupBy by argument().pair()
    private val ctx by requireObject<MutableMap<String, Any>>()

    override fun run() {
        val dataSets = ctx["data-sets"] as List<*>
        if (dataSets.size > 1) {
            echo("Para o comando group-by apenas um data-set é permitido")
            return
        }

        val group = groupBy.first
        val into = groupBy.second

        val data = dataSets.first() as Data

        ctx["data-sets"] = listOf(data.groupBy(group, into))
    }
}