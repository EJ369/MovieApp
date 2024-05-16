package com.example.movieapp

import org.junit.Test

import org.junit.Assert.*
class MovieUnitTest {
    @Test
    fun testGenreMapping() {
        // Your mapping logic
        val genresList = listOf(35, 18, 12)
        val mappedGenres = genresList.mapNotNull { Genres.mapIdToName(it) }

        // Assert the mapped genres
        assertEquals(listOf("Comedy", "Drama", "Adventure"), mappedGenres)
    }
}