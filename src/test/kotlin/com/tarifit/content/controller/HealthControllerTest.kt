package com.tarifit.content.controller

import com.tarifit.content.service.HealthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(HealthController::class)
class HealthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var healthService: HealthService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun healthService(): HealthService = mockk()
    }

    @Test
    fun `health endpoint should return service status`() {
        // Given
        val healthStatus = mapOf(
            "status" to "UP",
            "service" to "tarifit-content-service"
        )
        every { healthService.getHealthStatus() } returns healthStatus

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/health")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("tarifit-content-service"))
    }
}
