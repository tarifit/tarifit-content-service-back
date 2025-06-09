package com.tarifit.content.domain.dictionary

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "dictionary_waryaghri")
data class DictionaryWaryaghri(
    @Id override val id: String? = null,
    @Field("Tifinagh") val tifinagh: String? = null,
    @Field("Mot") val mot: String,
    @Field("DefinitionFr") val definitionFr: String,
    @Field("Type") val type: List<String>? = null,
    @Field("Pluriel") val pluriel: String? = null,
    @Field("EtatAnnexion") val etatAnnexion: String? = null,
    @Field("Synonyme") val synonyme: String? = null
) : DictionaryEntry
