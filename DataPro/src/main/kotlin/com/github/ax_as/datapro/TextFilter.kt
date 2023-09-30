package com.github.ax_as.datapro


class TextFilter {

    val firstBoldTag = "</?b>".toRegex(setOf(RegexOption.DOT_MATCHES_ALL))
    val lastBoldTag =
        ">b</?".toRegex(setOf(RegexOption.DOT_MATCHES_ALL))

    fun filter(
        text: String,
        start: String = "",
        end: String = "",
        excludeStart: Boolean = true,
        includeEnd: Boolean = false,
    ): String {
        if (start == "" && end == "") {
            return text
        }

        var firstStart = text.indexOf(start)
        if (firstStart < 0) {
            throw Error("[$start] não encontrado")
        }
        if (!excludeStart) {
            firstStart += start.length
        }

        var lastEnd = text.lastIndexOf(end)
        if (lastEnd < 0) {
            throw Error("[$end] não encontrado")
        }
        if (includeEnd) {
            lastEnd += end.length
        }

        if (lastEnd < firstStart) {
            throw Error("end [$lastEnd] aparece antes de start [$firstStart] ")
        }

        return text.substring(maxOf(0, firstStart), minOf(text.length, lastEnd))
    }

    private fun buildRegex(start: String, end: String): Regex {
        val escapedStart = Regex.escape(start)
        if (end == "") {
            val s = "(${escapedStart}.*?)$"
            return s.toRegex(setOf(RegexOption.DOT_MATCHES_ALL))
        }
        val escapedEnd = Regex.escape(end)
        if (start == "") {
            val s = "^(.*)(?=${escapedEnd})"
            print(s)
            return s.toRegex(setOf(RegexOption.DOT_MATCHES_ALL))
        }


        return "(${escapedStart})(.*?)(?=${escapedEnd})".toRegex(
            setOf(
                RegexOption.DOT_MATCHES_ALL
            )
        )
    }

}