package com.tarifit.content.repository.dictionary

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DictionaryAqeleiRepository : MongoRepository<DictionaryAqelei, String> {
    
    @Query("{\$or: [{'word': {\$regex: ?0, \$options: 'i'}}, {'translation': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByWordOrTranslationContainingIgnoreCase(query: String, pageable: Pageable): Page<DictionaryAqelei>
    
    @Query("{'word': {\$regex: ?0, \$options: 'i'}}")
    fun findByWordContainingIgnoreCase(word: String, pageable: Pageable): Page<DictionaryAqelei>
    
    @Query("{'translation': {\$regex: ?0, \$options: 'i'}}")
    fun findByTranslationContainingIgnoreCase(translation: String, pageable: Pageable): Page<DictionaryAqelei>
}
