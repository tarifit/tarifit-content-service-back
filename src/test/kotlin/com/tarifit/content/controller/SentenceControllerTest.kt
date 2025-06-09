package com.tarifit.content.controller

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.service.SentenceService
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

@WebMvcTest(SentenceController::class)
@Import(SentenceControllerTest.TestConfig::class)
class SentenceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var sentenceService: SentenceService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun sentenceService(): SentenceService = mockk()
    }

    @Test
    fun `searchSentences should return search results`() {
        // Given
        val query = "hello"
        val page = 0
        val size = 20
        val mockResult = PageImpl(
            listOf(Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")),
            PageRequest.of(page, size),
            1
        )
        every { sentenceService.searchSentences(query, page, size) } returns mockResult

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

        verify { sentenceService.searchSentences(query, page, size) }
    }

    @Test
    fun `searchSentences should use default parameters`() {
        // Given
        val query = "test"
        val mockResult = PageImpl(
            listOf(Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")),
            PageRequest.of(0, 20),
            1
        )
        every { sentenceService.searchSentences(query, 0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { sentenceService.searchSentences(query, 0, 20) }
    }

    @Test
    fun `getAllSentences should return paginated results`() {
        // Given
        val page = 1
        val size = 10
        val mockResult = PageImpl(
            listOf(Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")),
            PageRequest.of(page, size),
            1
        )
        every { sentenceService.getAllSentences(page, size) } returns mockResult

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

        verify { sentenceService.getAllSentences(page, size) }
    }

    @Test
    fun `getAllSentences should use default parameters`() {
        // Given
        val mockResult = PageImpl(
            listOf(Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")),
            PageRequest.of(0, 20),
            1
        )
        every { sentenceService.getAllSentences(0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { sentenceService.getAllSentences(0, 20) }
    }

    @Test
    fun `getRandomSentences should return random sentences`() {
        // Given
        val count = 5
        val mockResult = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        )
        every { sentenceService.getRandomSentences(count) } returns mockResult

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

        verify { sentenceService.getRandomSentences(count) }
    }

    @Test
    fun `getRandomSentences should use default count`() {
        // Given
        val mockResult = listOf(
            Sentence(id = "1", englishSentence = "Test", rifSentence = "Test")
        )
        every { sentenceService.getRandomSentences(10) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/sentences/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { sentenceService.getRandomSentences(10) }
    }
}
