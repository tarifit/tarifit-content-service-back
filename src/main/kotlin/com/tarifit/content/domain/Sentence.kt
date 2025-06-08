package com.tarifit.content.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "sentences")
data class Sentence(
    @Id val id: String? = null,
    val englishSentence: String,
    val rifSentence: String,
    val status: String? = null,
    val requiredVerifications: Int? = null,
    val correctVotesCount: Int? = null,
    val incorrectVotesCount: Int? = null,
    val needsEditVotesCount: Int? = null,
    val badTranslationVotesCount: Int? = null,
    val totalVerificationsCount: Int? = null,
    val lastReviewedAt: Instant? = null,
    val createdAt: Instant? = null,
    val difficultyLevel: String? = null,
    val sourceLanguages: List<String>? = null,
    val targetLanguages: List<String>? = null
)
