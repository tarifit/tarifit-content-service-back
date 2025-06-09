package com.tarifit.content.repository.verb

import com.tarifit.content.domain.verb.Verb
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VerbRepository : MongoRepository<Verb, String> {
    
    @Query("{\$or: [{'verb': {\$regex: ?0, \$options: 'i'}}, {'translation': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByVerbOrTranslationContainingIgnoreCase(query: String, pageable: Pageable): Page<Verb>
}
