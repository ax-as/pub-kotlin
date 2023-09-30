package com.github.ax_as.datapro.data

class GerenciadorConvocacoes<T : Convocador<*>>(private val listas: List<T>) {

    fun convocar(vaga: Int): Any {
        for (c in listas) {
            val candidato = c.convocar(vaga)
            if (candidato != null) {
                return candidato
            }
        }
        return Convocador.empty.convocar(vaga)
    }

}