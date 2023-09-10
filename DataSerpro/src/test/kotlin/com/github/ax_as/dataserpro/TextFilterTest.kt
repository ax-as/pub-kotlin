package com.github.ax_as.dataserpro

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.sign

class TextFilterTest {
    val textFilter = TextFilter()

    val text =
        "Lorem Ipsum is simply end2 dummy text of the printing and typesetting " +
                "industry. Lorem Ipsum has been the industry's standard dummy text ever " +
                "since the 1500s, start1 when an unknown printer end1 took a galley of type and scrambled \n" +
                "it to make a type specimen book. It has survived not only five centuries, " +
                "but also the leap into electronic typesetting, remaining essentially unchanged. \n\n" +
                "It was popularised in the 1960s with the release of Letraset sheets containing " +
                "Lorem Ipsum passages, and more recently with desktop publishing software like " +
                "Aldus PageMaker start2 including versions of Lorem Ipsum."


    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun filter_both_exists() {
        val r = textFilter.filter(text, "start1", "end1")
        assertEquals(listOf("start1 when an unknown printer"), r)
    }

    @Test
    fun filter_only_start_exists() {
        val r = textFilter.filter(text, "start2", "xx")
        assertEquals(listOf<String>(), r)
    }

    @Test
    fun filter_only_end_exists() {
        val r = textFilter.filter(text, "xx", "end2")
        assertEquals(listOf<String>(), r)
    }

    @Test
    fun filter_start_exists_end_empty() {
        val r = textFilter.filter(text, "start2")
        assertEquals(listOf("start2 including versions of Lorem Ipsum."), r)
    }

    @Test
    fun filter_end_exists_start_empty() {
        val r = textFilter.filter(text, end = "end2")
        assertEquals(listOf("Lorem Ipsum is simply"), r)
    }

    @Test
    fun filter_both_empty() {
        val r = textFilter.filter(text)
        assertLinesMatch(listOf(text), r)
    }

    @Test
    fun filter_both_not_exists() {
        val r = textFilter.filter(text, "xx", "yy")
        assertEquals(listOf<String>(), r)
    }

    @Test
    fun filter_only_start_multiple() {
        val text =
            "Lorem Ipsum startm1 is simply startm1 dummy text of the printing and startm1 typesetting."
        val r = textFilter.filter(text, "startm1")
        println(r.size)
        assertLinesMatch(
            listOf("startm1 is simply startm1 dummy text of the printing and startm1 typesetting."),
            r
        )
    }

    @Test
    fun filter_only_end_multiple() {
        val text =
            "Lorem Ipsum endm1 is simply endm1 dummy text of the printing endm1 typesetting."

        val r = textFilter.filter(text, end = "endm1")
        println(r.size)
        assertLinesMatch(
            listOf("Lorem Ipsum endm1 is simply endm1 dummy text of the printing"),
            r
        )
    }

    @Test
    fun filter_both_multiple() {
        val text =
            "startm1 Lorem Ipsum endm1 is simply startm1 dummy text of the printing endm1 typesetting."

        val r = textFilter.filter(text, "startm1", "endm1")
        println(r.size)
        assertLinesMatch(
            listOf(
                "startm1 Lorem Ipsum",
                "startm1 dummy text of the printing"
            ),
            r
        )

    }

    @Test
    fun filter_both_multiple2() {
        val text =
            "startm1 Lorem Ipsum \nstartm1 endm1 is simply startm1 dummy text of the printing endm1 typesetting."

        val r = textFilter.filter(text, "startm1", "endm1")
        println(r.size)
        assertLinesMatch(
            listOf(
                "startm1 Lorem Ipsum \nstartm1",
                "startm1 dummy text of the printing"
            ),
            r
        )
    }

    @Test
    fun filter_both_multiple3() {
        val text =
            "startm1 Lorem Ipsum startm1 x \nendm1 is simply\n startm1 dummy text of the printing endm1 typesetting. endm1"

        val r = textFilter.filter(text, "startm1", "endm1")
        println(r.size)
        assertLinesMatch(
            listOf(
                "startm1 Lorem Ipsum startm1 x",
                "startm1 dummy text of the printing"
            ),
            r
        )
    }

    @Test
    fun filter_both_multiple_multiline() {
        val text =
            "startm1 Lorem Ipsum startm1 x endm1 is simply startm1 dummy \ntext of the printing endm1 typesetting. endm1"

        val r = textFilter.filter(text, "startm1", "endm1")
        println(r.size)
        assertEquals(
            listOf(
                "startm1 Lorem Ipsum startm1 x",
                "startm1 dummy \ntext of the printing"
            ),
            r
        )
    }

    @Test
    fun filter_with_bold() {
        val text =
            "startb1 startm1 Lorem Ipsum </b> startm1 <b> x endm1 is simply startm1 </b> dummy \ntext of <b> the printing endm1 typesetting.</b><b>  endb1"

        val r =
            textFilter.filter(text, "startb1", "endb1", includeStart = false)
        println(r.size)
        assertEquals(
            listOf(
                "<b> startm1 Lorem Ipsum </b> startm1 <b> x endm1 is simply startm1 </b> dummy \n" +
                        "text of <b> the printing endm1 typesetting.</b><b></b>"
            ),
            r
        )
    }

}