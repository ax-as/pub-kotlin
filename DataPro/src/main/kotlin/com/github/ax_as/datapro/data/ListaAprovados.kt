package com.github.ax_as.datapro.data

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow

class ListaAprovados(
    val primeira: Int = 1,
    val passo: Int = 0,
    val candidatos: DataFrame<*>
) : Convocador<DataRow<*>?> {
    private var proximo = primeira
    private var indice = 0
    override fun convocar(vaga: Int): DataRow<*>? {
        if (proximo != vaga && passo != 0) {
            return null
        }
        if (indice < candidatos.rowsCount()) {
            proximo += passo
            return candidatos[indice++]
        }

        return null
    }
}