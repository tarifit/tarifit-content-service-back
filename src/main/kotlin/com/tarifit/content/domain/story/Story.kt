package com.tarifit.content.domain.story

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "stories")
data class Story(
    @Id val id: String? = null,
    val title: String,
    @Field("tarifit_text") val tarifitText: String,
    val themes: List<String> = emptyList(),
    val category: String? = null,
    val author: String? = null,
    @Field("difficulty_level") val difficultyLevel: String? = null
)
