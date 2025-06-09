package com.tarifit.content.repository.story

import com.tarifit.content.domain.story.Story
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StoryRepository : MongoRepository<Story, String> {
    
    @Query("{\$or: [{'title': {\$regex: ?0, \$options: 'i'}}, {'title_english': {\$regex: ?0, \$options: 'i'}}, {'title_french': {\$regex: ?0, \$options: 'i'}}, {'tarifit_text': {\$regex: ?0, \$options: 'i'}}, {'english_text': {\$regex: ?0, \$options: 'i'}}, {'french_text': {\$regex: ?0, \$options: 'i'}}, {'author': {\$regex: ?0, \$options: 'i'}}]}")
    fun findByContentContainingIgnoreCase(query: String, pageable: Pageable): Page<Story>
    
    @Query("{'author': {\$regex: ?0, \$options: 'i'}}")
    fun findByAuthorContainingIgnoreCase(author: String, pageable: Pageable): Page<Story>
    
    @Query("{'category': ?0}")
    fun findByCategory(category: String, pageable: Pageable): Page<Story>
    
    @Query("{'difficulty_level': ?0}")
    fun findByDifficultyLevel(difficultyLevel: String, pageable: Pageable): Page<Story>
    
    @Query("{'themes': {\$in: [?0]}}")
    fun findByThemesContaining(theme: String, pageable: Pageable): Page<Story>
}
