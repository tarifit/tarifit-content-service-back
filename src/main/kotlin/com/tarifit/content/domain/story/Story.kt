package com.tarifit.content.domain.story

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "stories")
data class Story(
    @Id val id: String? = null,
    val title: String,
    @Field("title_english") val titleEnglish: String? = null,
    @Field("title_french") val titleFrench: String? = null,
    @Field("tarifit_text") val tarifitText: String,
    @Field("english_text") val englishText: String? = null,
    @Field("french_text") val frenchText: String? = null,
    @Field("difficulty_level") val difficultyLevel: String,
    val category: String,
    @Field("word_count") val wordCount: Int,
    @Field("estimated_reading_time") val estimatedReadingTime: Int,
    val author: String,
    @Field("publication_date") val publicationDate: String,
    val themes: List<String>,
    @Field("created_at") val createdAt: String
)
