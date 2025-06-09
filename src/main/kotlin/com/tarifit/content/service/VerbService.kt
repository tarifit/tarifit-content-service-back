package com.tarifit.content.service

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.repository.verb.VerbRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class VerbService(private val verbRepository: VerbRepository) {

    fun searchVerbs(query: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByVerbOrTranslationContainingIgnoreCase(query, pageable)
    }

    fun getAllVerbs(page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findAll(pageable)
    }

    fun getVerbById(id: String): Verb? {
        return verbRepository.findById(id).orElse(null)
    }

    fun getRandomVerbs(count: Int = 10): List<Verb> {
        val pageable: Pageable = PageRequest.of(0, count)
        return verbRepository.findAll(pageable).content
    }

    fun countAllVerbs(): Long {
        return verbRepository.count()
    }

    fun searchVerbsByConjugation(conjugation: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByConjugationsContaining(conjugation, pageable)
    }

    fun searchVerbsByParticipe(participe: String, page: Int = 0, size: Int = 20): Page<Verb> {
        val pageable: Pageable = PageRequest.of(page, size)
        return verbRepository.findByParticipesContaining(participe, pageable)
    }
}