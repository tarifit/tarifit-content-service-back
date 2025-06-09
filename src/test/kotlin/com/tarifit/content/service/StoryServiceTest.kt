package com.tarifit.content.service

import com.tarifit.content.domain.story.Story
import com.tarifit.content.repository.story.StoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StoryServiceTest {

    private lateinit var storyRepository: StoryRepository
    private lateinit var storyService: StoryService

    @BeforeEach
    fun setup() {
        storyRepository = mockk()
        storyService = StoryService(storyRepository)
    }

    @Test
    fun `searchStories should return paginated results`() {
        val stories = listOf(
            Story(id = "1", title = "Test Story", tarifitText = "Test content", themes = listOf("adventure"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 1)
        every { storyRepository.findByContentContainingIgnoreCase("test", any()) } returns page

        val result = storyService.searchStories("test")

        assertEquals(1, result.totalElements)
        assertEquals("Test Story", result.content[0].title)
        verify { storyRepository.findByContentContainingIgnoreCase("test", any()) }
    }

    @Test
    fun `getAllStories should return all stories with pagination`() {
        val stories = listOf(
            Story(id = "1", title = "Story 1", tarifitText = "Content 1", themes = listOf("adventure")),
            Story(id = "2", title = "Story 2", tarifitText = "Content 2", themes = listOf("romance"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 2)
        every { storyRepository.findAll(any<PageRequest>()) } returns page

        val result = storyService.getAllStories()

        assertEquals(2, result.totalElements)
        assertEquals(2, result.content.size)
        verify { storyRepository.findAll(any<PageRequest>()) }
    }

    @Test
    fun `getRandomStories should return limited number of stories`() {
        val stories = listOf(
            Story(id = "1", title = "Random Story", tarifitText = "Random content", themes = listOf("mystery"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 5), 1)
        every { storyRepository.findAll(PageRequest.of(0, 5)) } returns page

        val result = storyService.getRandomStories(5)

        assertEquals(1, result.size)
        assertEquals("Random Story", result[0].title)
        verify { storyRepository.findAll(PageRequest.of(0, 5)) }
    }

    @Test
    fun `getStoriesByCategory should filter by category`() {
        val stories = listOf(
            Story(id = "1", title = "Adventure Story", tarifitText = "Adventure content", 
                  category = "adventure", themes = listOf("action"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 1)
        every { storyRepository.findByCategory("adventure", any()) } returns page

        val result = storyService.getStoriesByCategory("adventure")

        assertEquals(1, result.totalElements)
        assertEquals("adventure", result.content[0].category)
        verify { storyRepository.findByCategory("adventure", any()) }
    }

    @Test
    fun `getStoriesByDifficulty should filter by difficulty level`() {
        val stories = listOf(
            Story(id = "1", title = "Easy Story", tarifitText = "Simple content", 
                  difficultyLevel = "beginner", themes = listOf("basic"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 1)
        every { storyRepository.findByDifficultyLevel("beginner", any()) } returns page

        val result = storyService.getStoriesByDifficulty("beginner")

        assertEquals(1, result.totalElements)
        assertEquals("beginner", result.content[0].difficultyLevel)
        verify { storyRepository.findByDifficultyLevel("beginner", any()) }
    }

    @Test
    fun `getStoriesByAuthor should filter by author name`() {
        val stories = listOf(
            Story(id = "1", title = "Author Story", tarifitText = "Author content", 
                  author = "John Doe", themes = listOf("biography"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 1)
        every { storyRepository.findByAuthorContainingIgnoreCase("john", any()) } returns page

        val result = storyService.getStoriesByAuthor("john")

        assertEquals(1, result.totalElements)
        assertEquals("John Doe", result.content[0].author)
        verify { storyRepository.findByAuthorContainingIgnoreCase("john", any()) }
    }

    @Test
    fun `getStoriesByTheme should filter by theme`() {
        val stories = listOf(
            Story(id = "1", title = "Themed Story", tarifitText = "Themed content", 
                  themes = listOf("romance", "drama"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 20), 1)
        every { storyRepository.findByThemesContaining("romance", any()) } returns page

        val result = storyService.getStoriesByTheme("romance")

        assertEquals(1, result.totalElements)
        assertEquals(listOf("romance", "drama"), result.content[0].themes)
        verify { storyRepository.findByThemesContaining("romance", any()) }
    }

    @Test
    fun `getStoryById should return story when found`() {
        val story = Story(id = "1", title = "Found Story", tarifitText = "Found content", themes = listOf("test"))
        every { storyRepository.findById("1") } returns Optional.of(story)

        val result = storyService.getStoryById("1")

        assertEquals("Found Story", result?.title)
        verify { storyRepository.findById("1") }
    }

    @Test
    fun `getStoryById should return null when not found`() {
        every { storyRepository.findById("999") } returns Optional.empty()

        val result = storyService.getStoryById("999")

        assertNull(result)
        verify { storyRepository.findById("999") }
    }

    @Test
    fun `searchStories should handle custom pagination`() {
        val stories = listOf(
            Story(id = "1", title = "Custom Story", tarifitText = "Custom content", themes = listOf("custom"))
        )
        val page = PageImpl(stories, PageRequest.of(1, 5), 1)
        every { storyRepository.findByContentContainingIgnoreCase("custom", PageRequest.of(1, 5)) } returns page

        val result = storyService.searchStories("custom", 1, 5)

        assertEquals(1, result.totalElements)
        verify { storyRepository.findByContentContainingIgnoreCase("custom", PageRequest.of(1, 5)) }
    }

    @Test
    fun `getAllStories should handle custom pagination`() {
        val stories = emptyList<Story>()
        val page = PageImpl(stories, PageRequest.of(2, 10), 0)
        every { storyRepository.findAll(PageRequest.of(2, 10)) } returns page

        val result = storyService.getAllStories(2, 10)

        assertEquals(0, result.totalElements)
        verify { storyRepository.findAll(PageRequest.of(2, 10)) }
    }

    @Test
    fun `getRandomStories should handle different count values`() {
        val stories = listOf(
            Story(id = "1", title = "Random 1", tarifitText = "Content 1", themes = listOf("test")),
            Story(id = "2", title = "Random 2", tarifitText = "Content 2", themes = listOf("test"))
        )
        val page = PageImpl(stories, PageRequest.of(0, 15), 2)
        every { storyRepository.findAll(PageRequest.of(0, 15)) } returns page

        val result = storyService.getRandomStories(15)

        assertEquals(2, result.size)
        verify { storyRepository.findAll(PageRequest.of(0, 15)) }
    }
}
