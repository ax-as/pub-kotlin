package com.github.ax_as.datapro.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ax_as.datapro.data.Data

abstract class BaseCmd(
    name: String?, invokeWithoutSubcommand: Boolean = true,
    allowMultipleSubcommands: Boolean = false
) : DataHolder, CliktCommand(
    name = name,
    invokeWithoutSubcommand = invokeWithoutSubcommand,
    allowMultipleSubcommands = allowMultipleSubcommands
) {
    private val ctx by findOrSetObject<MutableMap<String, Data>> { mutableMapOf() }

    override val dataCtx
        get() = this.ctx




}