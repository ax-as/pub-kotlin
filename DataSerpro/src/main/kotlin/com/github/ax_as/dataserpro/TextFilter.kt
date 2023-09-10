package com.github.ax_as.dataserpro


class TextFilter {

    val firstBoldTag = "</?b>".toRegex(setOf(RegexOption.DOT_MATCHES_ALL))
    val lastBoldTag =
        ">b</?".toRegex(setOf(RegexOption.DOT_MATCHES_ALL))

    fun filter(
        text: String,
        start: String = "",
        end: String = "",
        includeStart: Boolean = true,
    ): List<String> {
        if (start == "" && end == "") {
            return arrayListOf(text)
        }

        val rx: Regex = buildRegex(start, end)
        return rx.findAll(text).map {
            var modValue = it.value.trim()

            if (!includeStart) {
                modValue = modValue.replaceFirst(
                    Regex.escape(start)
                        .toRegex(setOf(RegexOption.DOT_MATCHES_ALL)), ""
                )
            }

            val fbt = firstBoldTag.find(modValue)
            fbt?.let { mr ->
                if (mr.value == "</b>") {
                    modValue = "<b>$modValue"
                }
            }
            modValue = modValue.reversed()
            val lbt = lastBoldTag.find(modValue)
            lbt?.let { mr ->
                if (mr.value == "<b>".reversed()) {
                    modValue = "</b>".reversed()+modValue
                }
            }
            modValue.reversed().trim()
        }.toList()
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