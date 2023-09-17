package com.github.ax_as.datapro.cmd

import com.github.ax_as.datapro.data.Data

interface DataHolder {
    val dataCtx: MutableMap<String, Data>
    
    fun DataHolder.addFiles(
        map: MutableMap<String, MutableSet<String>>,
        it: String
    ): String {
        val (key, value) = it.split("=").let { Pair(it[0], it[1]) }
        val l = map[key] ?: mutableSetOf()
        l.add(value)

        map[key] = l
        return it
    }

    fun DataHolder.dataByName(name: String): Data {
        return dataCtx[name] ?: findByBestMatch(dataCtx, name)
    }

    fun DataHolder.dataByIndex(id: Int): Data {
        return dataCtx.toList()[id].second
    }

    fun DataHolder.put(key: String, data: Data) {
        dataCtx[key] = data
    }



    fun DataHolder.put(data: Data) {
        dataCtx[data.name] = data
    }

    fun DataHolder.forEach(fe: (String, Data) -> Data) {
        val updated = mutableSetOf<Data>()
        dataCtx.keys.forEach{
            val p = dataCtx[it]?:return
            val data = fe(it, p)
//            put(p.name, data)
            updated.add(data)
        }
        for (data in updated) {
            put(data.name, data)
        }


    //        dataCtx.forEach{p ->
//
//        }
    }


    fun DataHolder.first(): Data? {
        return dataCtx[dataCtx.keys.first()]
    }


    fun DataHolder.forIt(dataName: String, fi: (String, Data) -> Data) {
        val df = dataCtx[dataName]
        df?.let {
            val d = fi(it.name, it)
            put(it.name, d)
        }
    }

    fun DataHolder.count(): Int {
        return dataCtx.size
    }

    private fun findByBestMatch(
        dataCtx: MutableMap<String, Data>,
        name: String
    ): Data {
        val d = dataCtx.filter { it.key.startsWith(name) }.map { it.value }
        if (d.size == 1) {
            return d.first()
        }

        throw Error("objeto data não encontrado ou não distinguível para a chave $name")
    }


}