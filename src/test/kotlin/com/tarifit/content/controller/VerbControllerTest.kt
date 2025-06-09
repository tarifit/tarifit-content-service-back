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
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(VerbController::class)
@Import(VerbControllerTest.TestConfig::class)
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
        val verbs = listOf(
            Verb(verb = "aca", translation = "sentir", conjugations = mapOf("necc" to mapOf("prétérit" to "uciɣ")))
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbService.searchVerbs("aca", 0, 20) } returns page

        mockMvc.perform(
            get("/api/v1/content/verbs/search")
                .param("q", "aca")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0].verb").value("aca"))
            .andExpect(jsonPath("$.content[0].translation").value("sentir"))

        verify { verbService.searchVerbs("aca", 0, 20) }
    }

    @Test
    fun `getAllVerbs should return paginated results`() {
        val verbs = listOf(
            Verb(verb = "test", translation = "test")
        )
        val page = PageImpl(verbs, PageRequest.of(1, 10), 1)
        every { verbService.getAllVerbs(1, 10) } returns page

        mockMvc.perform(
            get("/api/v1/content/verbs")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].verb").value("test"))

        verify { verbService.getAllVerbs(1, 10) }
    }

    @Test
    fun `getVerbById should return verb when found`() {
        val verb = Verb(id = "1", verb = "found", translation = "trouvé")
        every { verbService.getVerbById("1") } returns verb

        mockMvc.perform(
            get("/api/v1/content/verbs/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.verb").value("found"))
            .andExpect(jsonPath("$.translation").value("trouvé"))

        verify { verbService.getVerbById("1") }
    }

    @Test
    fun `getVerbById should return 404 when not found`() {
        every { verbService.getVerbById("999") } returns null

        mockMvc.perform(
            get("/api/v1/content/verbs/999")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)

        verify { verbService.getVerbById("999") }
    }

    @Test
    fun `getRandomVerbs should return random verbs`() {
        val verbs = listOf(
            Verb(verb = "random1", translation = "aléatoire1"),
            Verb(verb = "random2", translation = "aléatoire2")
        )
        every { verbService.getRandomVerbs(5) } returns verbs

        mockMvc.perform(
            get("/api/v1/content/verbs/random")
                .param("count", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].verb").value("random1"))
            .andExpect(jsonPath("$[1].verb").value("random2"))

        verify { verbService.getRandomVerbs(5) }
    }

    @Test
    fun `getVerbCount should return count`() {
        every { verbService.countAllVerbs() } returns 42L

        mockMvc.perform(
            get("/api/v1/content/verbs/count")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(42))

        verify { verbService.countAllVerbs() }
    }

    @Test
    fun `searchByConjugation should return search results`() {
        val verbs = listOf(
            Verb(verb = "conj", translation = "conjugated")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbService.searchVerbsByConjugation("pattern", 0, 20) } returns page

        mockMvc.perform(
            get("/api/v1/content/verbs/search/conjugation")
                .param("q", "pattern")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].verb").value("conj"))

        verify { verbService.searchVerbsByConjugation("pattern", 0, 20) }
    }

    @Test
    fun `searchByParticipe should return search results`() {
        val verbs = listOf(
            Verb(verb = "part", translation = "participe")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbService.searchVerbsByParticipe("participe", 0, 20) } returns page

        mockMvc.perform(
            get("/api/v1/content/verbs/search/participe")
                .param("q", "participe")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].verb").value("part"))

        verify { verbService.searchVerbsByParticipe("participe", 0, 20) }
    }

    @Test
    fun `getAllVerbs should use default parameters`() {
        val page = PageImpl(emptyList<Verb>(), PageRequest.of(0, 20), 0)
        every { verbService.getAllVerbs(0, 20) } returns page

        mockMvc.perform(
            get("/api/v1/content/verbs")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { verbService.getAllVerbs(0, 20) }
    }

    @Test
    fun `getRandomVerbs should use default count`() {
        val verbs = emptyList<Verb>()
        every { verbService.getRandomVerbs(10) } returns verbs

        mockMvc.perform(
            get("/api/v1/content/verbs/random")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)

        verify { verbService.getRandomVerbs(10) }
    }
}