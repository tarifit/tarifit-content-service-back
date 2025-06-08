package com.tarifit.content.service

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.domain.verb.Verb
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ContentService(
    private val dictionaryService: DictionaryService,
    private val sentenceService: SentenceService,
    private val verbService: VerbService
) {

    // Dictionary operations
    fun searchDictionary(query: String, dictionaryType: String = "aqelei", page: Int = 0, size: Int = 20): Page<*> {
        return when (dictionaryType.lowercase()) {
            "aqelei" -> dictionaryService.searchAqelei(query, page, size)
            "waryaghri" -> dictionaryService.searchWaryaghri(query, page, size)
            else -> dictionaryService.searchAqelei(query, page, size)
        }
    }

    fun getRandomWords(dictionaryType: String = "aqelei", count: Int = 10): List<*> {
        return when (dictionaryType.lowercase()) {
            "aqelei" -> dictionaryService.getRandomAqeleiWords(count)
            "waryaghri" -> dictionaryService.getRandomWaryaghriWords(count)
            else -> dictionaryService.getRandomAqeleiWords(count)
        }
    }

    fun getAllDictionaryEntries(dictionaryType: String = "aqelei", page: Int = 0, size: Int = 20): Page<*> {
        return when (dictionaryType.lowercase()) {
            "aqelei" -> dictionaryService.getAllAqelei(page, size)
            "waryaghri" -> dictionaryService.getAllWaryaghri(page, size)
            else -> dictionaryService.getAllAqelei(page, size)
        }
    }

    // Sentence operations
    fun searchSentences(query: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        return sentenceService.searchSentences(query, page, size)
    }

    fun getAllSentences(page: Int = 0, size: Int = 20): Page<Sentence> {
        return sentenceService.getAllSentences(page, size)
    }

    fun getRandomSentences(count: Int = 10): List<Sentence> {
        return sentenceService.getRandomSentences(count)
    }

    // Verb operations
    fun searchVerbs(query: String, page: Int = 0, size: Int = 20): Page<Verb> {
        return verbService.searchVerbs(query, page, size)
    }

    fun getAllVerbs(page: Int = 0, size: Int = 20): Page<Verb> {
        return verbService.getAllVerbs(page, size)
    }

    fun getRandomVerbs(count: Int = 10): List<Verb> {
        return verbService.getRandomVerbs(count)
    }
}
