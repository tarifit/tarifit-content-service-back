package com.tarifit.content.service

import com.tarifit.content.domain.story.Story
import com.tarifit.content.repository.story.StoryRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class StoryService(
    private val storyRepository: StoryRepository
) {

    @Cacheable("story-search")
    fun searchStories(query: String, page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findByContentContainingIgnoreCase(query, pageable)
    }

    fun getAllStories(page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findAll(pageable)
    }

    @Cacheable("random-stories")
    fun getRandomStories(count: Int = 10): List<Story> {
        val pageable: Pageable = PageRequest.of(0, count)
        return storyRepository.findAll(pageable).content
    }

    fun getStoriesByCategory(category: String, page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findByCategory(category, pageable)
    }

    fun getStoriesByDifficulty(difficulty: String, page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findByDifficultyLevel(difficulty, pageable)
    }

    fun getStoriesByAuthor(author: String, page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findByAuthorContainingIgnoreCase(author, pageable)
    }

    fun getStoriesByTheme(theme: String, page: Int = 0, size: Int = 20): Page<Story> {
        val pageable: Pageable = PageRequest.of(page, size)
        return storyRepository.findByThemesContaining(theme, pageable)
    }

    fun getStoryById(id: String): Story? {
        return storyRepository.findById(id).orElse(null)
    }
}
