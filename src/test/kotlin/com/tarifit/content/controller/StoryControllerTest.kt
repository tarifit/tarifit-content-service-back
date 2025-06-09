package com.tarifit.content.controller

import com.tarifit.content.domain.story.Story
import com.tarifit.content.service.StoryService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(StoryController::class)
@Import(StoryControllerTest.TestConfig::class)
class StoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var storyService: StoryService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun storyService(): StoryService = mockk()
    }

    @Test
    fun `searchStories should return search results`() {
        // Given
        val query = "Massin"
        val page = 0
        val size = 20
        val mockResult = PageImpl(
            listOf(
                Story(
                    id = "1", 
                    title = "Azul, nec qqaren-ayi Massin",
                    tarifitText = "Azul, nec qqaren-ayi Massin...",
                    difficultyLevel = "intermediate",
                    category = "autobiographical",
                    author = "Fuad (Amaziɣ Massin)",
                    themes = listOf("identity", "language", "culture"),
                )
            ),
            PageRequest.of(page, size),
            1
        )
        every { storyService.searchStories(query, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/search")
                .param("q", query)
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].title").value("Azul, nec qqaren-ayi Massin"))
            .andExpect(jsonPath("$.content[0].author").value("Fuad (Amaziɣ Massin)"))

        verify { storyService.searchStories(query, page, size) }
    }

    @Test
    fun `searchStories should use default parameters`() {
        // Given
        val query = "identity"
        val mockResult = PageImpl(
            listOf(
                Story(
                    id = "1", 
                    title = "Test Story",
                    tarifitText = "Test content",
                    difficultyLevel = "beginner",
                    category = "test",
                    author = "Test Author",
                    themes = listOf("identity"),
                )
            ),
            PageRequest.of(0, 20),
            1
        )
        every { storyService.searchStories(query, 0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { storyService.searchStories(query, 0, 20) }
    }

    @Test
    fun `getAllStories should return paginated results`() {
        // Given
        val page = 1
        val size = 10
        val mockResult = PageImpl(
            listOf(
                Story(
                    id = "1", 
                    title = "Test Story",
                    tarifitText = "Test content",
                    difficultyLevel = "intermediate",
                    category = "autobiographical",
                    author = "Test Author",
                    themes = listOf("test"),
                )
            ),
            PageRequest.of(page, size),
            1
        )
        every { storyService.getAllStories(page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories")
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].title").value("Test Story"))

        verify { storyService.getAllStories(page, size) }
    }

    @Test
    fun `getAllStories should use default parameters`() {
        // Given
        val mockResult = PageImpl(
            listOf(
                Story(
                    id = "1", 
                    title = "Default Story",
                    tarifitText = "Default content",
                    difficultyLevel = "beginner",
                    category = "test",
                    author = "Default Author",
                    themes = listOf("default"),
                )
            ),
            PageRequest.of(0, 20),
            1
        )
        every { storyService.getAllStories(0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { storyService.getAllStories(0, 20) }
    }

    @Test
    fun `getRandomStories should return random stories`() {
        // Given
        val count = 5
        val mockResult = listOf(
            Story(
                id = "1", 
                title = "Random Story 1",
                tarifitText = "Random content 1",
                difficultyLevel = "beginner",
                category = "fiction",
                author = "Random Author 1",
                themes = listOf("random"),
            ),
            Story(
                id = "2", 
                title = "Random Story 2",
                tarifitText = "Random content 2",
                difficultyLevel = "intermediate",
                category = "historical",
                author = "Random Author 2",
                themes = listOf("history"),
            )
        )
        every { storyService.getRandomStories(count) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/random")
                .param("count", count.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Random Story 1"))
            .andExpect(jsonPath("$[1].title").value("Random Story 2"))

        verify { storyService.getRandomStories(count) }
    }

    @Test
    fun `getRandomStories should use default count`() {
        // Given
        val mockResult = listOf(
            Story(
                id = "1", 
                title = "Default Random Story",
                tarifitText = "Default random content",
                difficultyLevel = "beginner",
                category = "test",
                author = "Default Author",
                themes = listOf("default"),
            )
        )
        every { storyService.getRandomStories(10) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { storyService.getRandomStories(10) }
    }

    @Test
    fun `getStoriesByCategory should return stories filtered by category`() {
        // Given
        val category = "autobiographical"
        val page = 0
        val size = 10
        val mockResult = PageImpl(
            listOf(
                Story(
                    id = "1", 
                    title = "Autobiographical Story",
                    tarifitText = "Personal story content",
                    difficultyLevel = "intermediate",
                    category = category,
                    author = "Personal Author",
                    themes = listOf("identity", "personal"),
                )
            ),
            PageRequest.of(page, size),
            1
        )
        every { storyService.getStoriesByCategory(category, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/category/$category")
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].category").value(category))

        verify { storyService.getStoriesByCategory(category, page, size) }
    }

    @Test
    fun `getStoryById should return story when found`() {
        // Given
        val storyId = "massin_identity_story_2024"
        val mockStory = Story(
            id = storyId, 
            title = "Azul, nec qqaren-ayi Massin",
            tarifitText = "Azul, nec qqaren-ayi Massin...",
            difficultyLevel = "intermediate",
            category = "autobiographical",
            author = "Fuad (Amaziɣ Massin)",
            themes = listOf("identity", "language", "culture", "immigration", "family"),
        )
        every { storyService.getStoryById(storyId) } returns mockStory

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/$storyId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(storyId))
            .andExpect(jsonPath("$.title").value("Azul, nec qqaren-ayi Massin"))

        verify { storyService.getStoryById(storyId) }
    }

    @Test
    fun `getStoryById should return 404 when story not found`() {
        // Given
        val storyId = "nonexistent_story"
        every { storyService.getStoryById(storyId) } returns null

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/stories/$storyId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)

        verify { storyService.getStoryById(storyId) }
    }
}
