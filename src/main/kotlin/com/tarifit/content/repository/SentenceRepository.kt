package com.tarifit.content.repository

import com.tarifit.content.domain.Sentence
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SentenceRepository : MongoRepository<Sentence, String> {
    
    @Query("{\$or: [{'englishSentence': {\$regex: ?0, \$options: 'i'}}, {'rifSentence': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByEnglishOrRifSentenceContainingIgnoreCase(query: String, pageable: Pageable): Page<Sentence>
    
    @Query("{'englishSentence': {\$regex: ?0, \$options: 'i'}}")
    fun findByEnglishSentenceContainingIgnoreCase(englishSentence: String, pageable: Pageable): Page<Sentence>
    
    @Query("{'rifSentence': {\$regex: ?0, \$options: 'i'}}")
    fun findByRifSentenceContainingIgnoreCase(rifSentence: String, pageable: Pageable): Page<Sentence>
}
