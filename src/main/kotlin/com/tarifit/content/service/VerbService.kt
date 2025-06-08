package com.tarifit.content.service

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.repository.verb.VerbRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class VerbService(
    private val verbRepository: VerbRepository
) {

    fun searchVerbs(query: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByVerbOrTranslationContainingIgnoreCase(query, pageable)
    }

    fun searchByVerb(verb: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByVerbContainingIgnoreCase(verb, pageable)
    }

    fun searchByTranslation(translation: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByTranslationContainingIgnoreCase(translation, pageable)
    }

    fun getAllVerbs(page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findAll(pageable)
    }

    fun getRandomVerbs(count: Int = 10): List<Verb> {
        val pageable: Pageable = PageRequest.of(0, count)
        return verbRepository.findAll(pageable).content
    }

    fun getVerbsByDifficulty(difficulty: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByDifficulty(difficulty, pageable)
    }
}
