package com.github.ax_as.datapro.data

class DataFiles {
    companion object {
        private val dataDir = "/data/files/tratados/"
        private val suffix = ".e.csv"

        val injusticados = "${dataDir}injusticados$suffix"
        val funcionarios_serpro = "${dataDir}funcionarios_serpro$suffix"

        val bb = "${dataDir}bb$suffix"
        val serpro_pcd = "${dataDir}serpro_final_pcd$suffix"
        val serpro_ac = "${dataDir}serpro_final_ac$suffix"
        val serpro_ppp = "${dataDir}serpro_final_ppp$suffix"
        val serpro_final_pratica = "${dataDir}pratica_final_definitivo_ac$suffix"
        val serpro_final_pratica_ppp = "${dataDir}pratica_final_definitivo_ppp$suffix"
        val serpro_final_pratica_pcd = "${dataDir}pratica_final_definitivo_pcd$suffix"

        val objetivas_ac = "${dataDir}objetivas_ac$suffix"
        val objetivas_ppp = "${dataDir}objetivas_ppp$suffix"
        val objetivas_pcd = "${dataDir}objetivas_pcd$suffix"

        val serpro_prov_pratica = "${dataDir}pratica_prov_ac$suffix"

        val serpro_prov_pratica_ppp = "${dataDir}pratica_prov_ppp$suffix"
        val serpro_prov_pratica_pcd = "${dataDir}pratica_prov_pcd$suffix"
    }
}
