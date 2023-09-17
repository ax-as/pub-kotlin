package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.parameters.options.*
import com.github.ax_as.datapro.data.Data
import mu.KotlinLogging

class PrintCmd() : BaseCmd(name = "print") {
    private val colFilter by option("--cols").split(",")
    val logger = KotlinLogging.logger {}
    private val dataFrameName by option("--data").default("")
    private val concat by option().flag("--concat", default = false)
    override fun run() {
        logger.debug { "PrintCmd" }
        if (concat) {
            val name = dataCtx.keys.first()
            val concData = Data.concat(name, dataCtx.values.map { it })
            dataCtx.keys.clear()
            put(concData)
        }
        val cols = colFilter?: arrayListOf()

        cols.let {
            if (dataFrameName.isEmpty()) {
                forEach { _, d ->
                    if (it.isEmpty()){
                        d.print()
                    }else{
                        d.print(it)
                    }
                    d
                }
                return
            }

            forIt(dataFrameName) { _, df ->
                df.print(it, true)
                df
            }
        }
    }
}