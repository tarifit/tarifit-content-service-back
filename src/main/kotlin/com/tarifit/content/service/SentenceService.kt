package com.tarifit.content.service

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.repository.sentence.SentenceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SentenceService(
    private val sentenceRepository: SentenceRepository
) {

    fun searchSentences(query: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable)
    }

    fun searchEnglishSentences(englishQuery: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findByEnglishSentenceContainingIgnoreCase(englishQuery, pageable)
    }

    fun searchRifSentences(rifQuery: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findByRifSentenceContainingIgnoreCase(rifQuery, pageable)
    }

    fun getAllSentences(page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findAll(pageable)
    }

    fun getRandomSentences(count: Int = 10): List<Sentence> {
        val pageable: Pageable = PageRequest.of(0, count)
        return sentenceRepository.findAll(pageable).content
    }
}
