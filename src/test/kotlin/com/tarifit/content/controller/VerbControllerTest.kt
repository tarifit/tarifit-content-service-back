package com.tarifit.content.controller

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.service.VerbService
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
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(VerbController::class)
class VerbControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var verbService: VerbService

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun verbService(): VerbService = mockk()
    }

    @Test
    fun `searchVerbs should return search results`() {
        // Given
        val query = "run"
        val page = 0
        val size = 20
        val mockResult = PageImpl(
            listOf(Verb(id = "1", verb = "azwel", translation = "run")),
            PageRequest.of(page, size),
            1
        )
        every { verbService.searchVerbs(query, page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs/search")
                .param("q", query)
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].verb").value("azwel"))
            .andExpect(jsonPath("$.content[0].translation").value("run"))

        verify { verbService.searchVerbs(query, page, size) }
    }

    @Test
    fun `searchVerbs should use default parameters`() {
        // Given
        val query = "test"
        val mockResult = PageImpl(
            listOf(Verb(id = "1", verb = "test", translation = "test")),
            PageRequest.of(0, 20),
            1
        )
        every { verbService.searchVerbs(query, 0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { verbService.searchVerbs(query, 0, 20) }
    }

    @Test
    fun `getAllVerbs should return paginated results`() {
        // Given
        val page = 1
        val size = 10
        val mockResult = PageImpl(
            listOf(Verb(id = "1", verb = "azwel", translation = "run")),
            PageRequest.of(page, size),
            1
        )
        every { verbService.getAllVerbs(page, size) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs")
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].verb").value("azwel"))

        verify { verbService.getAllVerbs(page, size) }
    }

    @Test
    fun `getAllVerbs should use default parameters`() {
        // Given
        val mockResult = PageImpl(
            listOf(Verb(id = "1", verb = "test", translation = "test")),
            PageRequest.of(0, 20),
            1
        )
        every { verbService.getAllVerbs(0, 20) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { verbService.getAllVerbs(0, 20) }
    }

    @Test
    fun `getRandomVerbs should return random verbs`() {
        // Given
        val count = 5
        val mockResult = listOf(
            Verb(id = "1", verb = "azwel", translation = "run"),
            Verb(id = "2", verb = "ssiwel", translation = "walk")
        )
        every { verbService.getRandomVerbs(count) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs/random")
                .param("count", count.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].verb").value("azwel"))
            .andExpect(jsonPath("$[1].verb").value("ssiwel"))

        verify { verbService.getRandomVerbs(count) }
    }

    @Test
    fun `getRandomVerbs should use default count`() {
        // Given
        val mockResult = listOf(
            Verb(id = "1", verb = "test", translation = "test")
        )
        every { verbService.getRandomVerbs(10) } returns mockResult

        // When & Then
        mockMvc.perform(
            get("/api/v1/content/verbs/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { verbService.getRandomVerbs(10) }
    }
}
