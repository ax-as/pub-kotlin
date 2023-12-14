package com.github.ax_as.datapro.cmd

import com.github.ax_as.datapro.data.Data
import java.nio.file.Path
import java.util.*

interface CsvFilesHolder {
    val csv: MutableMap<String, MutableSet<String>>

    fun sepConvert(c: Char): Char {
        return when (c) {
            'c' -> ','
            'p' -> '|'
            's' -> ' '
            else -> c
        }

    }


    fun loadAndMerge(
        name: String,
        files: List<Path>,
        delimiter: Char
    ): Data {
        val datas = arrayListOf<Data>()
        for (file in files) {
            datas.add(
                Data.fromCsv(
                    name = name,
                    path = file,
                    locale = Locale.US,
                    delimiter = delimiter,
                )
            )
        }

        return Data.mergeOf(datas)
    }
}