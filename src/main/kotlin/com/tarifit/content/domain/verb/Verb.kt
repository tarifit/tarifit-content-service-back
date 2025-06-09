package com.tarifit.content.domain.verb

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "verbs")
data class Verb(
    @Id val id: String? = null,
    val verb: String,
    val translation: String,
    val conjugations: Map<String, Map<String, String>> = emptyMap(),
    val participes: Map<String, String> = emptyMap()
)
