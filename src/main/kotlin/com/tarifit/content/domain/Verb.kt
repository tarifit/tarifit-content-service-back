package com.tarifit.content.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "verbs")
data class Verb(
    @Id val id: String? = null,
    val verb: String,
    val translation: String,
    val conjugations: Map<String, Map<String, String>> = emptyMap(),
    val participes: Map<String, String>? = null,
    val difficulty: String? = null,
    val isActive: Boolean? = null,
    val lastUpdated: Instant? = null,
    val morphologyClass: String? = null,
    val tags: List<String>? = null,
    val type: String? = null
)
