package com.github.ax_as.datapro.data

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DataFrameExtTest {
    val defaultColNames =
        arrayListOf("Nome", "Ano de Criação", "Idade", "Número de Obras")

    @Test
    fun filterCols() {
        val df = createSampleDataFrame()
        var c = df.filterCols("Ano")
        assertEquals(arrayListOf("Ano de Criação"), c.map { c.toString() })

        c = df.filterCols("N")
        assertEquals(arrayListOf("Nome", "Número de Obras"), c.map { c.toString() })

        c = df.filterCols("+nome")
        assertEquals(arrayListOf("Nome"), c.map { c.toString() })

    }

    @Test
    fun search() {
        val df = createSampleDataFrame()
        val r = df.search("Nome", "pluto")
        val data = listOf("Pluto", "1930", "91", "100+")

        assertEquals(
            dataFrameOf(header = defaultColNames, values = data),
            r
        )
    }


    private fun createSampleDataFrame(): DataFrame<*> {
        val nomes = listOf(
            "Mickey",
            "Minnie",
            "Donald",
            "Daisy",
            "Goofy",
            "Pluto",
            "Simba",
            "Timon",
            "Pumbaa",
            "Ariel",
            "Eric",
            "Flounder",
            "Jasmine",
            "Aladdin",
            "Genie",
            "Mulan",
            "Peter Pan",
            "Tinker Bell",
            "Captain Hook",
            "Elsa"
        )

        val criacao = listOf(
            "1928",
            "1928",
            "1934",
            "1940",
            "1932",
            "1930",
            "1994",
            "1994",
            "1994",
            "1989",
            "1989",
            "1989",
            "1992",
            "1992",
            "1992",
            "1998",
            "1953",
            "1953",
            "1953",
            "2013",
        )

        val idade = arrayListOf(
            "93",
            "93",
            "87",
            "81",
            "89",
            "91",
            "29",
            "29",
            "29",
            "32",
            "32",
            "32",
            "29",
            "29",
            "29",
            "23",
            "68",
            "68",
            "68",
            "8"
        )

        val obras = listOf(
            "400+",
            "400+",
            "350+",
            "150+",
            "150+",
            "100+",
            "6",
            "6",
            "6",
            "10",
            "10",
            "10",
            "6",
            "6",
            "6",
            "3",
            "5",
            "5",
            "5",
            "4"
        )
        return dataFrameOf(
            "Nome" to nomes,
            "Ano de Criação" to criacao,
            "Idade" to idade,
            "Número de Obras" to obras
        )
    }
}