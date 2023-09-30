package com.github.ax_as.datapro

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TextFilterTest {
    val textFilter = TextFilter()


    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun filter_both_exists() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test, "start1", "end1")
        assertEquals("start1 dummy text of the printing and ", r)
    }
    @Test
    fun filter_both_equals() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test, "start1", "start1")
        assertEquals("", r)
    }

    @Test
    fun filter_both_exists_invert() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(
            test,
            "start1",
            "end1",
            excludeStart = false,
            includeEnd = true
        )
        assertEquals(" dummy text of the printing and end1", r)
    }

    @Test
    fun filter_only_start_exists() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        assertThrows<Error> { textFilter.filter(test, "start1", "xx") }
        assertThrows<Error> {
            textFilter.filter(
                test,
                "start1",
                "xx",
                excludeStart = false,
                includeEnd = true
            )
        }
    }

    @Test
    fun filter_only_end_exists() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        assertThrows<Error> { textFilter.filter(test, "xx", "end2") }
        assertThrows<Error> {
            textFilter.filter(
                test,
                "xx",
                "end2",
                excludeStart = false,
                includeEnd = true
            )
        }
    }

    @Test
    fun filter_end_exists_start_empty() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test, end = "end1")
        assertEquals(
            "Lorem Ipsum is simply start1 dummy text of the printing and ",
            r
        )
    }

    @Test
    fun filter_end_exists_start_empty_invert() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(
            test,
            end = "end1",
            excludeStart = false,
            includeEnd = true
        )
        assertEquals(
            "Lorem Ipsum is simply start1 dummy text of the printing and end1",
            r
        )
    }

    @Test
    fun filter_start_exists_end_empty() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test, "start1")
        assertEquals(
            "start1 dummy text of the printing and end1 typesetting",
            r
        )
    }

    @Test
    fun filter_start_after_end() {
        val test =
            "Lorem Ipsum is simply end1start1 dummy text of the printing and  typesetting "
        assertThrows<Error> {
            textFilter.filter(test, "start1", "end1")
        }
    }

    @Test
    fun filter_start_after_end_invert() {
        val test =
            "Lorem Ipsum is simply end1start1 dummy text of the printing and  typesetting "
        assertThrows<Error> {
            textFilter.filter(
                test,
                "start1",
                "end1",
                excludeStart = false,
                includeEnd = true
            )
        }
    }

    @Test
    fun filter_start_exists_end_empty_invert() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(
            test,
            "start1",
            excludeStart = false,
            includeEnd = true
        )
        assertEquals(
            " dummy text of the printing and end1 typesetting",
            r
        )
    }

    @Test
    fun filter_both_empty() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test)
        assertEquals(test, r)
    }

    @Test
    fun filter_both_empty_invert() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        val r = textFilter.filter(test, excludeStart = false, includeEnd = true)
        assertEquals(test, r)
    }

    @Test
    fun filter_both_not_exists() {
        val test =
            "Lorem Ipsum is simply start1 dummy text of the printing and end1 typesetting "
        assertThrows<Error> { textFilter.filter(test, "xx", "yy") }
        assertThrows<Error> {
            textFilter.filter(
                test,
                "xx",
                "yy",
                excludeStart = false,
                includeEnd = true
            )
        }
    }

}