package com.tarifit.content.service

import org.springframework.stereotype.Service

@Service
class HealthService {

    fun getHealthStatus(): Map<String, String> {
        return mapOf(
            "status" to "UP",
            "service" to "tarifit-content-service"
        )
    }
}
