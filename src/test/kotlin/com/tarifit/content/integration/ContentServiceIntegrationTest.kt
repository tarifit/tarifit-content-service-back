package com.tarifit.content.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration") 
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
class ContentServiceIntegrationTest {
    
    @Test
    fun `application context loads successfully`() {
        // Basic smoke test to verify Spring context loads
        // Real integration tests require MONGODB_URI environment variable
    }
}
