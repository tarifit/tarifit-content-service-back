package com.tarifit.content.controller

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.service.ContentService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(SentenceController::class)
class SentenceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var contentService: ContentService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun contentService(): ContentService = mockk()
    }

    @Test
    fun `searchSentences should return search results`() {
        // Given
        val query = "hello"
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { contentService.searchSentences(query, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/search")
                .param("q", query)
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].englishSentence").value("Hello"))
            .andExpect(jsonPath("$.content[0].rifSentence").value("Azul"))

        verify { contentService.searchSentences(query, page, size) }
    }

    @Test
    fun `searchSentences should use default parameters`() {
        // Given
        val query = "test"
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")
        ))
        every { contentService.searchSentences(query, 0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { contentService.searchSentences(query, 0, 20) }
    }

    @Test
    fun `getAllSentences should return paginated results`() {
        // Given
        val page = 1
        val size = 10
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { contentService.getAllSentences(page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences")
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].englishSentence").value("Hello"))

        verify { contentService.getAllSentences(page, size) }
    }

    @Test
    fun `getAllSentences should use default parameters`() {
        // Given
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")
        ))
        every { contentService.getAllSentences(0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { contentService.getAllSentences(0, 20) }
    }

    @Test
    fun `getRandomSentences should return random sentences`() {
        // Given
        val count = 5
        val mockResult = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        )
        every { contentService.getRandomSentences(count) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/random")
                .param("count", count.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].englishSentence").value("Hello"))
            .andExpect(jsonPath("$[1].englishSentence").value("Goodbye"))

        verify { contentService.getRandomSentences(count) }
    }

    @Test
    fun `getRandomSentences should use default count`() {
        // Given
        val mockResult = listOf(
            Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")
        )
        every { contentService.getRandomSentences(10) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { contentService.getRandomSentences(10) }
    }
}
