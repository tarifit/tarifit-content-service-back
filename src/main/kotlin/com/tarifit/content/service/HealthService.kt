package com.tarifit.content.service

import org.springframework.stereotype.Service
import java.time.Instant

@Service
class HealthService {

    fun getHealthStatus(): Map<String, Any> {
        return mapOf(
            "status" to "UP",
            "service" to "tarifit-content-service",
            "timestamp" to Instant.now(),
            "version" to "1.0.0"
        )
    }
}
