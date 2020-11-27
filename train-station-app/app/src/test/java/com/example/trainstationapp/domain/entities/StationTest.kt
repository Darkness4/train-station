package com.example.trainstationapp.domain.entities

import com.example.trainstationapp.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class StationTest : WordSpec({

    "toggleFavorite" should {
        "turn isFavorite to true if false" {
            // Arrange
            val station = TestUtils.createStation("0")
            val expect = !station.isFavorite

            // Act
            station.toggleFavorite()

            // Assert
            station.isFavorite shouldBe expect
        }
    }
})
