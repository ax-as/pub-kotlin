package com.github.ax_as.datapro

class TextFormatter {
    val regexBold = Regex("<b>(.*?)</b>", RegexOption.DOT_MATCHES_ALL)
    fun format(text: String) : String{
        val bolds = regexBold.findAll(text)
        for (bold in bolds) {
//            println(bold.value)
        }

        return text
    }

}