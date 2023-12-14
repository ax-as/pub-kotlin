package com.github.ax_as.datapro.data

import java.math.BigDecimal
import java.math.RoundingMode

data class Questoes(
    val q: Int,
    val item: Int,
    val total: Double,
    val nrItems: Int
) {
}

val p1 = Questoes(1, 1, 18.0, 4)
val p2 = Questoes(1, 2, 12.0, 2)

val js1 = Questoes(2, 1, 24.0, 3)
val js2 = Questoes(2, 2, 16.0, 3)

val j1 = Questoes(3, 1, 7.5, 3)
val j2 = Questoes(3, 2, 7.5, 3)
val j3 = Questoes(3, 3, 15.0, 3)

val itemsQuestoes = arrayListOf(p1, p2, js1, js2, j1, j2, j3)


fun calcular(notaProva: Float): List<Questoes> {
    val compartimentos = mutableMapOf<BigDecimal, MutableList<Questoes>>()
    for (iq in itemsQuestoes) {
        val r = BigDecimal.valueOf(iq.total)
            .divide(iq.nrItems.toBigDecimal(), 3, RoundingMode.HALF_UP)
        if (compartimentos[r] == null) {
            compartimentos[r] = mutableListOf()
        }
        compartimentos[r]?.add(iq)
    }
    val keys = compartimentos.keys.sorted()


    return arrayListOf()
}