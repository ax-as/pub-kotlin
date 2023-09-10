//package com.github.ax_as.dataserpro
//
//import org.jetbrains.letsPlot.geom.geomHistogram
//import org.jetbrains.letsPlot.ggsize
//import org.jetbrains.letsPlot.intern.Plot
//import org.jetbrains.letsPlot.letsPlot
//import java.awt.Desktop
//import java.io.File
//
//object DemoUtil {
//    fun createPlot(df: DF): Plot {
//
//        val toList = df.dataframe[FuncionariosSerpro.notaCEspecifico].toList()
//        val data = mapOf<String, List<*>>(
//            "x" to toList,
//            "y" to IntRange(0, 3917).map { "$it" }.toList()
//        )
//
//        val p = letsPlot(data) { x = "x"; y = "y" } + ggsize(500, 250)
//        return p + geomHistogram(binWidth = 1)
//    }
//
//    fun openInBrowser(content: String) {
//        val dir = File(System.getProperty("user.dir"), "lets-plot-images")
//        dir.mkdir()
//        val file = File(dir.canonicalPath, "my_plot.html")
//        file.createNewFile()
//        file.writeText(content)
//
//        Desktop.getDesktop().browse(file.toURI())
//    }
//}