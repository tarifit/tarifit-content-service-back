package com.tarifit.content.controller

import com.tarifit.content.service.HealthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/content")
class HealthController(private val healthService: HealthService) {

    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(healthService.getHealthStatus())
    }
}
