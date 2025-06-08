package com.tarifit.content.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "dictionary_aqelɛi")
data class DictionaryEntry(
    @Id val id: String? = null,
    val word: String,
    val plural: String? = null,
    val type: String? = null,
    val translation: String,
    val lang: String? = null,
    val computedDifficulty: String? = null,
    val difficultyReasoning: String? = null,
    val finalAnalysisDate: Instant? = null,
    val lastUpdated: Instant? = null,
    val scriptVersion: String? = null,
    val semanticAnalysisDate: Instant? = null,
    val semanticCategory: String? = null,
    val semanticConfidence: Double? = null,
    val semanticDifficulty: String? = null,
    val semanticReasoning: String? = null,
    val appearsInCorpus: Boolean? = null,
    val corpusAnalysisDate: Instant? = null,
    val corpusContexts: Int? = null,
    val difficultyMethod: String? = null,
    val grammaticalTypeLevel: String? = null
)
