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

    fun searchByEnglish(query: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findByEnglishSentenceContainingIgnoreCase(query, pageable)
    }

    fun searchByRif(query: String, page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findByRifSentenceContainingIgnoreCase(query, pageable)
    }

    fun getAllSentences(page: Int = 0, size: Int = 20): Page<Sentence> {
        val pageable: Pageable = PageRequest.of(page, size)
        return sentenceRepository.findAll(pageable)
    }

    fun getRandomSentences(count: Int = 10): List<Sentence> {
        val pageable: Pageable = PageRequest.of(0, count)
        return sentenceRepository.findAll(pageable).content
    }

    fun getSentenceById(id: String): Sentence? {
        val result = sentenceRepository.findById(id)
        return if (result.isPresent) result.get() else null
    }

    fun countAllSentences(): Long {
        return sentenceRepository.count()
    }
}
