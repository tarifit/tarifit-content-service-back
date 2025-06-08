package com.tarifit.content.domain.dictionary

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "dictionary_waryaghri")
data class DictionaryWaryaghri(
    @Id val id: String? = null,
    val tifinagh: String? = null,
    val mot: String,
    val definitionFr: String,
    val type: List<String>? = null,
    val pluriel: String? = null,
    val etatAnnexion: String? = null,
    val synonyme: String? = null
)
