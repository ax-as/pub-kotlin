package com.github.ax_as.dataserpro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ax_as.dataserpro.data.Data
import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.DataFrame

class SearchCmd() :
    CliktCommand(name = "search", invokeWithoutSubcommand = true) {
    private val col: String by argument()
    private val query: String by argument()
    private val ctx by requireObject<MutableMap<String, Any>>()
    override fun run() {
        val searchResult = mutableListOf<Data>()
        val dataSets = ctx["data-sets"] as List<*>

        dataSets.forEach {
            val ds = it as Data

            val res = ds.search(col, query)
            if (!res.isEmpty) {
                searchResult.add(res)
            }
        }

        ctx["data-sets"] = searchResult
    }
}