package com.tarifit.content.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HealthServiceTest {

    private lateinit var healthService: HealthService

    @BeforeEach
    fun setup() {
        healthService = HealthService()
    }

    @Test
    fun `getHealthStatus should return UP status with service info`() {
        val result = healthService.getHealthStatus()

        assertEquals("UP", result["status"])
        assertEquals("tarifit-content-service", result["service"])
    }

    @Test
    fun `getHealthStatus should always return consistent format`() {
        val result1 = healthService.getHealthStatus()
        val result2 = healthService.getHealthStatus()

        assertEquals(result1["status"], result2["status"])
        assertEquals(result1["service"], result2["service"])
        assertEquals(2, result1.size)
        assertEquals(2, result2.size)
    }

    @Test
    fun `getHealthStatus should contain required fields`() {
        val result = healthService.getHealthStatus()

        assert(result.containsKey("status"))
        assert(result.containsKey("service"))
        assertEquals("UP", result["status"])
        assertEquals("tarifit-content-service", result["service"])
    }

    @Test
    fun `getHealthStatus should return map with string values`() {
        val result = healthService.getHealthStatus()

        result.values.forEach { value ->
            assert(value is String) { "All values should be strings, but found: ${value::class}" }
        }
    }
}
