package com.github.ax_as.datapro.data

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*

class ConcursoSerpro(private val df: DataFrame<*>) {
    companion object {
        // @formatter:off
        const val pos = "pos"
        const val inscricao = "Inscrição"
        const val nome = "Nome"
        const val acertosPortugues = "Acertos Ling. Por."
        const val notaIngles = "Nota Ling. Inglesa"
        const val acertosIngles = "Acertos Ling. Inglesa"
        const val notaEstatistica = "Nota Estatistica"
        const val acertosEstatistica = "Acertos Estatística"
        const val notaRacLogico = "Nota Rac. Lógico"
        const val acertosRacLogic = "Acertos Rac. Lógico"
        const val notaLegislac = "Nota Legislação"
        const val acertosLeg = "Acertos Legislação"
        const val notaCBasicos = "Nota C. Básicos"
        const val acertosCBasicos = "Acertos C. Básicos"
        const val notaCEspecifico = "Nota C. Específicos"
        const val acertosCEspecific = "Acertos C. Específicos"
        const val notaParcial = "Nota Parcial"
        const val notaFinal = "Nota (Cálculo Final)"
        const val notaPort = "Nota Ling. Por."
        const val notaLinguas = "Soma Linguas"
         // @formatter:on



        private fun prefilter(df: DataFrame<*>): DataFrame<*> {
            val cols = arrayOf(
                nome,
                pos,
                inscricao,
                notaParcial,
                notaFinal,
            ).filter { df.containsColumn(it) }.toTypedArray()


            return df.head(Int.MAX_VALUE).select(*cols)
                .update(nome) {
                    it.toString().uppercase().trim()
                }
        }
    }

}
