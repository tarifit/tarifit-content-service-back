package com.tarifit.content.domain.dictionary

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "dictionaryType"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = DictionaryAqelei::class, name = "aqelei"),
    JsonSubTypes.Type(value = DictionaryWaryaghri::class, name = "waryaghri")
)
interface DictionaryEntry {
    val id: String?
}
