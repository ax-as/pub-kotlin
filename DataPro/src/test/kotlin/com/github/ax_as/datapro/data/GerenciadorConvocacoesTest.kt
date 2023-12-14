package com.github.ax_as.datapro.data

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.junit.jupiter.api.Test

class GerenciadorConvocacoesTest {

    var listaAC =
        DataFrame.concursoSerproObjetivas(DataFiles.objetivas_ac)
            .final_objetivas("AC")
            .final_objetivas_pond()

    var listaPPP =
        DataFrame.concursoSerproObjetivas(DataFiles.objetivas_ppp)
            .final_objetivas("PPP")
            .final_objetivas_pond()

    var listaPCD =
        DataFrame.concursoSerproObjetivas(DataFiles.objetivas_pcd)
            .final_objetivas("PCD")
            .final_objetivas_pond()


    val listaPpp = ListaAprovados(3, 5, listaPPP)
    val listaPcd = ListaAprovados(5, 5, listaPCD)
    val listaAc = ListaAprovados(1, 1, listaAC)


    val g = GerenciadorConvocacoes<Convocador<*>>(
        arrayListOf(
            listaPpp,
            listaPcd,
            listaAc
        )
    )

    @Test
    fun convocar() {
        var vaga = 1
        while (true){
            val c = g.convocar(vaga++)
            if (c == Convocador.emptyResponse) {
                break
            }
        }

    }
}