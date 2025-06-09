package com.tarifit.content.repository.dictionary

import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DictionaryWaryaghriRepository : MongoRepository<DictionaryWaryaghri, String> {
    
    @Query("{\$or: [{'Mot': {\$regex: ?0, \$options: 'i'}}, {'DefinitionFr': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByMotOrDefinitionContainingIgnoreCase(query: String, pageable: Pageable): Page<DictionaryWaryaghri>
}
