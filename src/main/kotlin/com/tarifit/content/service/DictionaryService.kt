package com.tarifit.content.service

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.repository.dictionary.DictionaryAqeleiRepository
import com.tarifit.content.repository.dictionary.DictionaryWaryaghriRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DictionaryService(
    private val aqeleiRepository: DictionaryAqeleiRepository,
    private val waryaghriRepository: DictionaryWaryaghriRepository
) {

    @Cacheable("dictionary-aqelei-search")
    fun searchAqelei(query: String, page: Int = 0, size: Int = 20): Page<DictionaryAqelei> {
        val pageable: Pageable = PageRequest.of(page, size)
        return aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(query, pageable)
    }

    @Cacheable("dictionary-waryaghri-search")
    fun searchWaryaghri(query: String, page: Int = 0, size: Int = 20): Page<DictionaryWaryaghri> {
        val pageable: Pageable = PageRequest.of(page, size)
        return waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(query, pageable)
    }

    @Cacheable("random-aqelei-words")
    fun getRandomAqeleiWords(count: Int = 10): List<DictionaryAqelei> {
        val pageable: Pageable = PageRequest.of(0, count)
        return aqeleiRepository.findAll(pageable).content
    }

    @Cacheable("random-waryaghri-words")
    fun getRandomWaryaghriWords(count: Int = 10): List<DictionaryWaryaghri> {
        val pageable: Pageable = PageRequest.of(0, count)
        return waryaghriRepository.findAll(pageable).content
    }

    fun getAllAqelei(page: Int = 0, size: Int = 20): Page<DictionaryAqelei> {
        val pageable: Pageable = PageRequest.of(page, size)
        return aqeleiRepository.findAll(pageable)
    }

    fun getAllWaryaghri(page: Int = 0, size: Int = 20): Page<DictionaryWaryaghri> {
        val pageable: Pageable = PageRequest.of(page, size)
        return waryaghriRepository.findAll(pageable)
    }
}
