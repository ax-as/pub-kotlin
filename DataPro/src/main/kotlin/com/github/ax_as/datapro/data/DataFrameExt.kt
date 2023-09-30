package com.github.ax_as.datapro.data

import org.jetbrains.kotlinx.dataframe.AnyCol
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.columns.ColumnReference
import org.jetbrains.kotlinx.dataframe.io.DataFrameHtmlData
import org.jetbrains.kotlinx.dataframe.io.DisplayConfiguration
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.toStandaloneHTML
import org.jetbrains.kotlinx.dataframe.name
import java.nio.file.Path
import java.text.Normalizer
import java.util.*
import kotlin.io.path.absolutePathString

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
const val defaultDir = "/data/files/"


fun DataFrame.Companion.fromLocal(
    filenames: List<String>,
    sep: Char,
): DataFrame<*> {
    return filenames.map {
        fromLocal(it, sep)
    }.concat()
}


fun DataFrame.Companion.concursoBB(localFile: String): DataFrame<*> {
    return DataFrame
        .fromLocal(localFile, '|', locale = Locale.forLanguageTag("PT"))
        .normalizeData("nome".toColumnAccessor()).remove("inscricao").distinct()
}

fun DataFrame.Companion.concursoSerpro(localFile: String): DataFrame<*> {
    return DataFrame.fromLocal(localFile, ',')
        .normalizeData("nome".toColumnAccessor())
//        .distinctBy("inscricao")
        .rename("notacb", "notace", "notapratica", "notaparcial")
        .into("N1", "N2", "N3", "Nobj").add("N2x2") { "N2"<Float>() * 2.0 }
        .add("NFC") {
            "N1"<Int>() + "N2x2"<Float>() + "N3"<Float>()
        }.remove("notafinalal")
}

fun DataFrame.Companion.concursoSerproObjetivas(localFile: String): DataFrame<*> {
    return DataFrame.fromLocal(localFile, ',', header = headerObjetivas)
        .normalizeData("nome".toColumnAccessor())
}


fun DataFrame<*>.final_objetivas(lista: String): DataFrame<*> {
    var posObj = 1

    return sumarizar(lista)
        .sortByDesc("NF_OBJ", "p2", "acce", "accb")

        .add("class_OBJ") { posObj++ }
}

fun DataFrame<*>.final_objetivas_pond(): DataFrame<*> {
    var pos = 1

    return sortByDesc("OBJ_POND", "p2", "acce", "accb")
        .add("class_OBJ_POND") { pos++ }
}

fun DataFrame<*>.add_prova_pratica_prov(pratica: DataFrame<*>): DataFrame<*> {
    var pos = 1
    return join(pratica, JoinType.Inner) {
        it["inscricao"] match right["inscricao"]
    }.add("nfc_prov") {
            "p3_prov"<Float>() + "OBJ_POND"<Float> ()
        }.sortByDesc("nfc_prov").add("class_nfc_prov"){pos ++}
}

fun DataFrame<*>.add_prova_pratica_final(pratica: DataFrame<*>): DataFrame<*> {
    var pos = 1
    return join(pratica, JoinType.Inner) {
        it["inscricao"] match right["inscricao"]
    }.add("NFC") {
        "p3"<Float>() + "OBJ_POND"<Float> ()
    }.sortByDesc("NFC", "p3", "p2", "acce", "accb").add("pos_final"){pos ++}
}


fun DataFrame<*>.sumarizar(lista: String): DataFrame<*> {
    return add("lista") {
        lista
    }.move("lista").to(0).add("OBJ_POND") {
        "p1"<Float>() + "p2"<Float>() * 2
    }
}

fun DataFrame<*>.removerZeradosPratica(): DataFrame<*>{
    val columns = "pos_final"
    return remove(columns).filter { "p3"<Float>() > 0 }.add(columns){index()+1}.moveToLeft(
        columns
    )
}

fun DataFrame<*>.eliminados_pratica(): DataFrame<*> {
    return add("eliminados_pratica") {
        if ("p1"<Float>() < 5 || "p2"<Float>() < 14) "sim" else ""
    }
}


val headerPratica = arrayOf("inscricao", "nome", "p3")
val headerPraticaProv = arrayOf("inscricao", "nome", "p3_prov")
val headerPraticaPontosGanhos = arrayOf(
    "inscricao",
    "nome",
    "p3",
    "pontos_ganhos_recurso",
    "lista"
)

val headerObjetivas = arrayOf(
    "inscricao",
    "nome",
    "np",
    "acp",
    "ni",
    "aci",
    "nes",
    "aces",
    "nrl",
    "acrl",
    "nl",
    "acl",
    "p1",
    "accb",
    "p2",
    "acce",
    "NF_OBJ"
)

val headerPraticaProvisorio = arrayOf(
    "inscricao",
    "nome",
    "p1",
    "accb",
    "p2",
    "acce",
    "NF_OBJ",
    "p3_prov",

    )

val headerPraticaFinal = arrayOf(
    "inscricao",
    "nome",
    "p1",
    "accb",
    "p2",
    "acce",
    "NF_OBJ",
    "p3_prov",
    "class_p3_prov"
) + headerPraticaProvisorio


fun DataFrame.Companion.praticaSerpro(
    localFile: String,
    lista: String,
    header: Array<String> = headerPratica
): DataFrame<*> {
    return DataFrame.fromLocal(localFile, ',', header = header)
        .normalizeData("nome".toColumnAccessor())
        .add("lista") { lista }
        .sortByDesc(header[2])
}

fun DataFrame.Companion.finalSerpro(localFile: String): DataFrame<*> {
    return DataFrame.fromLocal(localFile, '/')
        .normalizeData("nome".toColumnAccessor())
//        .distinctBy("inscricao")
        .rename("notacb", "notace", "notapratica", "notaparcial")
        .into("N1", "N2", "N3", "Nobj").add("N2x2") { "N2"<Float>() * 2.0 }
        .add("NFC") {
            "N1"<Int>() + "N2x2"<Float>() + "N3"<Float>()
        }.remove("notafinalal")
}

fun DataFrame<*>.sortByClassificacaoQualificada(): DataFrame<*> {
    return sortByDesc {
        it["N1"] and it["acertosce"] and it["acertoscb"]
    }.add("pos_objetivas") { index() + 1 }
}


fun DataFrame<*>.sortByFirstNameCount(): DataFrame<*> {
    return add("first_name") {
        it["nome"].toString().split(" ")[0]
    }.groupBy {
        it["first_name"]
    }.sortByCount().toDataFrame()
}

fun DataFrame<*>.sortByClassificacaoFinal(): DataFrame<*> {
    return filter { "N3"<Float>() > 0 }.sortByDesc {
        it["NFC"] and it["N3"] and it["N2"] and it["acertosce"] and it["acertoscb"]
    }.remove("pos").add("pos") { index() + 1 }.moveToLeft("pos")
}

fun DataFrame<*>.sortByResultadoFinal(): DataFrame<*> {
    return sortBy(colName = "notafinal", null, true)
}

fun DataFrame<*>.sortByPosDiff(): DataFrame<*> {
    return sortByClassificacaoQualificada().add("dif_pos") {
        "pos_objetivas"<Int>() - "pos"<Int>()
    }.sortBy(colName = "dif_pos", "pos_dif_pos", false).move { it["pos"] }
        .after { it["pos_objetivas"] }
}

fun DataFrame<*>.toDefaultHtml(): DataFrameHtmlData {
    return this.toStandaloneHTML(DisplayConfiguration(rowsLimit = null)).copy(
        style = DataFrame.defaultStyle()
    )

}

fun DataFrame<*>.sortBy(
    colName: String, addCol: String? = null, byDesc: Boolean
): DataFrame<*> {
    var r = if (byDesc) this.sortByDesc(colName) else this.sortBy(colName)
    addCol?.let {
        r = r.add(addCol) { index() + 1 }
    }
    return r
}

fun DataFrame.Companion.defaultStyle(): String {
    val iss =
        DataFrame::class.java.classLoader.getResourceAsStream("custom_table.css")
    return iss?.bufferedReader().use { it?.readText() ?: "" }
}



fun DataFrame.Companion.fromLocal(
    filename: String,
    sep: Char = ',',
    locale: Locale = Locale.US,
    header: Array<String> = arrayOf<String>()
): DataFrame<*> {
    val csvdf = DataFrame.readCSV(
        Path.of(filename).absolutePathString(),
        delimiter = sep,
        parserOptions = ParserOptions(
            locale = locale
        ),
        charset = Charsets.UTF_8,
        header = header.asList(),

        )

    return csvdf.rename { it.all() }.into { normalize(it) }
}


fun DataFrame<*>.normalizeData(c: ColumnReference<*>): DataFrame<*> {
    val nd = update(c) { row ->
        row.toString().lowercase().trim().split(" ").joinToString(" ") { sep ->
            sep.replaceFirstChar { fs ->
                fs.uppercaseChar()
            }
        }
    }
    return nd
}

fun DataFrame<*>.search(columnName: String, query: String): DataFrame<*> {
    val cols = findNormalizedCol(columnName)
    val r = mutableListOf<DataFrame<*>>()

    for (col in cols) {
        val df = this.filter {
            it[col].toString().lowercase().contains(query.lowercase())
        }
        r.add(df)
    }

    return r.concat()
}

fun DataFrame<*>.filterCols(s: String): List<AnyCol> {
    val r: List<AnyCol> = findNormalizedCol(s)
    return r
}

private fun DataFrame<*>.findExact(
    ncols: List<Pair<String, AnyCol>>, filter: String
): MutableList<AnyCol> {
    val r = mutableListOf<AnyCol>()
    for (ncol in ncols) {
        if (ncol.first == filter) {
            r.add(ncol.second)
        }
    }
    return r
}

private fun DataFrame<*>.findNormalizedCol(
    col: String
): List<AnyCol> {
    val lwcsStr = col.lowercase()
    val ncols = normalizedCols()
    val r: List<AnyCol>
    if (lwcsStr.startsWith("+")) {
        r = findExact(ncols, lwcsStr.split("+")[1])
    } else {
        r = mutableListOf()
        for (ncol in ncols) {
            if (ncol.first.startsWith(lwcsStr)) {
                r.add(ncol.second)
            }
        }
    }
    return r
}


private fun DataFrame<*>.normalizedCols(): List<Pair<String, DataColumn<*>>> {
    val ncols = this.columns().map {
        val nKey = normalize(it)
        Pair(nKey.lowercase(), it)
    }
    return ncols
}


private fun normalize(it: AnyCol) = Normalizer.normalize(
    it.name, Normalizer.Form.NFD
).replace(REGEX_UNACCENT, "")

