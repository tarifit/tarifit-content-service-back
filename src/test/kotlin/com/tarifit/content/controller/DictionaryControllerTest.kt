package com.tarifit.content.controller

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryEntry
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.service.DictionaryService
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(DictionaryController::class)
@Import(DictionaryControllerTest.TestConfig::class)
class DictionaryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var dictionaryService: DictionaryService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun dictionaryService(): DictionaryService = mockk()
    }

    @Test
    fun `searchDictionary should return search results`() {
        // Given
        val query = "water"
        val type = "aqelei"
        val page = 0
        val size = 20
        val mockResult: Page<DictionaryEntry> = PageImpl(
            listOf(DictionaryAqelei(id = "1", word = "eau", translation = "aman")),
            PageRequest.of(page, size),
            1
        )
        every { dictionaryService.searchDictionary(query, type, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", query)
                .param("type", type)
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].word").value("eau"))
            .andExpect(jsonPath("$.content[0].translation").value("aman"))

        verify { dictionaryService.searchDictionary(query, type, page, size) }
    }

    @Test
    fun `searchDictionary should use default parameters`() {
        // Given
        val query = "test"
        val mockResult: Page<DictionaryEntry> = PageImpl(
            listOf(DictionaryAqelei(id = "1", word = "test", translation = "test")),
            PageRequest.of(0, 20),
            1
        )
        every { dictionaryService.searchDictionary(query, "aqelei", 0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { dictionaryService.searchDictionary(query, "aqelei", 0, 20) }
    }

    @Test
    fun `getRandomWords should return random dictionary entries`() {
        // Given
        val type = "waryaghri"
        val count = 5
        val mockResult: List<DictionaryEntry> = listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau")
        )
        every { dictionaryService.getRandomWords(type, count) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/dictionary/random")
                .param("type", type)
                .param("count", count.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].mot").value("aman"))
            .andExpect(jsonPath("$[0].definitionFr").value("eau"))

        verify { dictionaryService.getRandomWords(type, count) }
    }

    @Test
    fun `getRandomWords should use default parameters`() {
        // Given
        val mockResult: List<DictionaryEntry> = listOf(
            DictionaryAqelei(id = "1", word = "test", translation = "test")
        )
        every { dictionaryService.getRandomWords("aqelei", 10) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/dictionary/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { dictionaryService.getRandomWords("aqelei", 10) }
    }

    @Test
    fun `getAllDictionaryEntries should return paginated results`() {
        // Given
        val type = "aqelei"
        val page = 1
        val size = 10
        val mockResult: Page<DictionaryEntry> = PageImpl(
            listOf(DictionaryAqelei(id = "1", word = "eau", translation = "aman")),
            PageRequest.of(page, size),
            1
        )
        every { dictionaryService.getAllDictionaryEntries(type, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/dictionary")
                .param("type", type)
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].word").value("eau"))

        verify { dictionaryService.getAllDictionaryEntries(type, page, size) }
    }

    @Test
    fun `searchDictionary should handle empty query`() {
        val mockResult: Page<DictionaryEntry> = PageImpl(emptyList())
        every { dictionaryService.searchDictionary("", "aqelei", 0, 20) } returns mockResult

        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", "")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `getRandomWords should handle large count parameter`() {
        val mockResult: List<DictionaryEntry> = emptyList()
        every { dictionaryService.getRandomWords("waryaghri", 1000) } returns mockResult

        mockMvc.perform(
            get("/api/v1/content/dictionary/random")
                .param("type", "waryaghri")
                .param("count", "1000")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `getAllDictionaryEntries should use default parameters`() {
        val mockResult: Page<DictionaryEntry> = PageImpl(emptyList())
        every { dictionaryService.getAllDictionaryEntries("aqelei", 0, 20) } returns mockResult

        mockMvc.perform(
            get("/api/v1/content/dictionary")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { dictionaryService.getAllDictionaryEntries("aqelei", 0, 20) }
    }
}
