package com.tarifit.content.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "dictionary_aqelɛi")
data class DictionaryEntry(
    @Id val id: String? = null,
    val word: String,
    val plural: String? = null,
    val type: String? = null,
    val translation: String,
    val lang: String? = null
)
