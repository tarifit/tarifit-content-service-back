package com.tarifit.content.controller

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.service.SentenceService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@WebMvcTest(SentenceController::class)
@TestPropertySource(properties = [
    "spring.data.mongodb.uri=mongodb://localhost:27017/testdb"
])
class SentenceControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var sentenceService: SentenceService

    @BeforeEach
    fun setup() {
        sentenceService = mockk()
        val controller = SentenceController(sentenceService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `searchSentences should return paginated sentences`() {
        val query = "hello"
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Hello world", rifSentence = "Azul amadal")
        )
        val page = PageImpl(sentences, PageRequest.of(0, 20), 1)
        every { sentenceService.searchSentences(query, 0, 20) } returns page

        mockMvc.perform(get("/api/v1/content/sentences/search")
            .param("q", query)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].englishSentence").value("Hello world"))
            .andExpect(jsonPath("$.content[0].rifSentence").value("Azul amadal"))
            .andExpect(jsonPath("$.totalElements").value(1))

        verify { sentenceService.searchSentences(query, 0, 20) }
    }

    @Test
    fun `searchSentences should handle custom pagination`() {
        val query = "test"
        val page = 2
        val size = 5
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Test sentence", rifSentence = "Asekki n tarmid")
        )
        val pageResult = PageImpl(sentences, PageRequest.of(page, size), 15)
        every { sentenceService.searchSentences(query, page, size) } returns pageResult

        mockMvc.perform(get("/api/v1/content/sentences/search")
            .param("q", query)
            .param("page", page.toString())
            .param("size", size.toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(15))
            .andExpect(jsonPath("$.number").value(page))
            .andExpect(jsonPath("$.size").value(size))

        verify { sentenceService.searchSentences(query, page, size) }
    }

    @Test
    fun `searchSentences should handle empty results`() {
        val query = "nonexistent"
        val emptyPage = PageImpl(emptyList<Sentence>(), PageRequest.of(0, 20), 0)
        every { sentenceService.searchSentences(query, 0, 20) } returns emptyPage

        mockMvc.perform(get("/api/v1/content/sentences/search")
            .param("q", query)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
            .andExpect(jsonPath("$.totalElements").value(0))

        verify { sentenceService.searchSentences(query, 0, 20) }
    }

    @Test
    fun `getAllSentences should return all sentences with default pagination`() {
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "First sentence", rifSentence = "Asekki amezwaru"),
            Sentence(id = "2", englishSentence = "Second sentence", rifSentence = "Asekki wis sin")
        )
        val page = PageImpl(sentences, PageRequest.of(0, 20), 2)
        every { sentenceService.getAllSentences(0, 20) } returns page

        mockMvc.perform(get("/api/v1/content/sentences")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].englishSentence").value("First sentence"))
            .andExpect(jsonPath("$.content[1].englishSentence").value("Second sentence"))
            .andExpect(jsonPath("$.totalElements").value(2))

        verify { sentenceService.getAllSentences(0, 20) }
    }

    @Test
    fun `getAllSentences should handle custom pagination`() {
        val page = 1
        val size = 10
        val sentences = listOf(
            Sentence(id = "11", englishSentence = "Eleventh sentence", rifSentence = "Asekki wis mraw d yan")
        )
        val pageResult = PageImpl(sentences, PageRequest.of(page, size), 25)
        every { sentenceService.getAllSentences(page, size) } returns pageResult

        mockMvc.perform(get("/api/v1/content/sentences")
            .param("page", page.toString())
            .param("size", size.toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(25))
            .andExpect(jsonPath("$.number").value(page))
            .andExpect(jsonPath("$.size").value(size))

        verify { sentenceService.getAllSentences(page, size) }
    }

    @Test
    fun `getAllSentences should handle empty repository`() {
        val emptyPage = PageImpl(emptyList<Sentence>(), PageRequest.of(0, 20), 0)
        every { sentenceService.getAllSentences(0, 20) } returns emptyPage

        mockMvc.perform(get("/api/v1/content/sentences")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
            .andExpect(jsonPath("$.totalElements").value(0))

        verify { sentenceService.getAllSentences(0, 20) }
    }

    @Test
    fun `getRandomSentences should return random sentences with default count`() {
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Random sentence 1", rifSentence = "Asekki amazray 1"),
            Sentence(id = "2", englishSentence = "Random sentence 2", rifSentence = "Asekki amazray 2")
        )
        every { sentenceService.getRandomSentences(10) } returns sentences

        mockMvc.perform(get("/api/v1/content/sentences/random")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].englishSentence").value("Random sentence 1"))
            .andExpect(jsonPath("$[1].englishSentence").value("Random sentence 2"))

        verify { sentenceService.getRandomSentences(10) }
    }

    @Test
    fun `getRandomSentences should handle custom count`() {
        val count = 25
        val sentences = (1..15).map {
            Sentence(id = "$it", englishSentence = "Sentence $it", rifSentence = "Asekki $it")
        }
        every { sentenceService.getRandomSentences(count) } returns sentences

        mockMvc.perform(get("/api/v1/content/sentences/random")
            .param("count", count.toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(15))

        verify { sentenceService.getRandomSentences(count) }
    }

    @Test
    fun `getRandomSentences should handle empty results`() {
        val count = 5
        every { sentenceService.getRandomSentences(count) } returns emptyList()

        mockMvc.perform(get("/api/v1/content/sentences/random")
            .param("count", count.toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))

        verify { sentenceService.getRandomSentences(count) }
    }

    @Test
    fun `searchSentences should handle special characters in query`() {
        val query = "hello@#$%"
        val emptyPage = PageImpl(emptyList<Sentence>(), PageRequest.of(0, 20), 0)
        every { sentenceService.searchSentences(query, 0, 20) } returns emptyPage

        mockMvc.perform(get("/api/v1/content/sentences/search")
            .param("q", query)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)

        verify { sentenceService.searchSentences(query, 0, 20) }
    }

    @Test
    fun `endpoints should handle CORS properly`() {
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "CORS test", rifSentence = "Armad n CORS")
        )
        val page = PageImpl(sentences, PageRequest.of(0, 20), 1)
        every { sentenceService.getAllSentences(0, 20) } returns page

        mockMvc.perform(get("/api/v1/content/sentences")
            .header("Origin", "http://localhost:3000")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(header().string("Access-Control-Allow-Origin", "*"))

        verify { sentenceService.getAllSentences(0, 20) }
    }
}
