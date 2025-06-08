package com.tarifit.content.domain.sentence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sentences")
data class Sentence(
    @Id val id: String? = null,
    val englishSentence: String,
    val rifSentence: String
)
