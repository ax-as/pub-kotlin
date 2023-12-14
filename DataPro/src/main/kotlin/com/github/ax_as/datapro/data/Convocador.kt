package com.github.ax_as.datapro.data

interface Convocador<T> {
    fun convocar(vaga: Int): T

    companion object {
        val emptyResponse: Any = object : Any(){}

        val empty: Convocador<Any> = object : Convocador<Any> {
            override fun convocar(vaga: Int) : Any{
                return emptyResponse
            }

        }
    }
}