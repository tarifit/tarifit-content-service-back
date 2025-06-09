package com.tarifit.content.repository.sentence

import com.tarifit.content.domain.sentence.Sentence
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SentenceRepository : MongoRepository<Sentence, String> {
    
    @Query("{\$or: [{'englishSentence': {\$regex: ?0, \$options: 'i'}}, {'rifSentence': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByEnglishOrRifSentenceContainingIgnoreCase(query: String, pageable: Pageable): Page<Sentence>
}
