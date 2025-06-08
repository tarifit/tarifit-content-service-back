package com.tarifit.content.integration

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.repository.dictionary.DictionaryAqeleiRepository
import com.tarifit.content.repository.dictionary.DictionaryWaryaghriRepository
import com.tarifit.content.repository.sentence.SentenceRepository
import com.tarifit.content.repository.verb.VerbRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class ContentServiceIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var aqeleiRepository: DictionaryAqeleiRepository

    @MockBean
    private lateinit var waryaghriRepository: DictionaryWaryaghriRepository

    @MockBean
    private lateinit var sentenceRepository: SentenceRepository

    @MockBean
    private lateinit var verbRepository: VerbRepository

    @BeforeEach
    fun setup() {
        // Setup mock data for repositories
        setupDictionaryMocks()
        setupSentenceMocks()
        setupVerbMocks()
    }

    private fun setupDictionaryMocks() {
        val aqeleiEntries = listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman"),
            DictionaryAqelei(id = "2", word = "pain", translation = "aghrum")
        )
        val waryaghriEntries = listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau"),
            DictionaryWaryaghri(id = "2", mot = "aghrum", definitionFr = "pain")
        )

        given(aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(
            anyString(), any(PageRequest::class.java)
        )).willReturn(PageImpl(aqeleiEntries))

        given(waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(
            anyString(), any(PageRequest::class.java)
        )).willReturn(PageImpl(waryaghriEntries))

        given(aqeleiRepository.findAll(any(PageRequest::class.java)))
            .willReturn(PageImpl(aqeleiEntries))

        given(waryaghriRepository.findAll(any(PageRequest::class.java)))
            .willReturn(PageImpl(waryaghriEntries))
    }

    private fun setupSentenceMocks() {
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        )

        given(sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(
            anyString(), any(PageRequest::class.java)
        )).willReturn(PageImpl(sentences))

        given(sentenceRepository.findAll(any(PageRequest::class.java)))
            .willReturn(PageImpl(sentences))
    }

    private fun setupVerbMocks() {
        val verbs = listOf(
            Verb(id = "1", verb = "azwel", translation = "run"),
            Verb(id = "2", verb = "ssiwel", translation = "walk")
        )

        given(verbRepository.findByVerbOrTranslationContainingIgnoreCase(
            anyString(), any(PageRequest::class.java)
        )).willReturn(PageImpl(verbs))

        given(verbRepository.findAll(any(PageRequest::class.java)))
            .willReturn(PageImpl(verbs))
    }

    @Test
    fun `full dictionary search workflow should work end-to-end`() {
        // Test Aqelei dictionary search
        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", "water")
                .param("type", "aqelei")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].word").value("eau"))
            .andExpect(jsonPath("$.content[0].translation").value("aman"))

        // Test Waryaghri dictionary search  
        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", "aman")
                .param("type", "waryaghri")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].mot").value("aman"))
            .andExpect(jsonPath("$.content[0].definitionFr").value("eau"))
    }

    @Test
    fun `full sentence workflow should work end-to-end`() {
        // Test sentence search
        mockMvc.perform(
            get("/api/v1/content/sentences/search")
                .param("q", "hello")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].englishSentence").value("Hello"))
            .andExpect(jsonPath("$.content[0].rifSentence").value("Azul"))

        // Test random sentences
        mockMvc.perform(
            get("/api/v1/content/sentences/random")
                .param("count", "1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].englishSentence").exists())
    }

    @Test
    fun `full verb workflow should work end-to-end`() {
        // Test verb search
        mockMvc.perform(
            get("/api/v1/content/verbs/search")
                .param("q", "run")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].verb").value("azwel"))
            .andExpect(jsonPath("$.content[0].translation").value("run"))

        // Test random verbs
        mockMvc.perform(
            get("/api/v1/content/verbs/random")
                .param("count", "1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].verb").exists())
    }

    @Test
    fun `health endpoint should be accessible`() {
        mockMvc.perform(
            get("/api/v1/content/health")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("tarifit-content-service"))
    }

    @Test
    fun `pagination should work correctly`() {
        // Test dictionary pagination
        mockMvc.perform(
            get("/api/v1/content/dictionary")
                .param("type", "aqelei")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.pageable").exists())

        // Test sentence pagination
        mockMvc.perform(
            get("/api/v1/content/sentences")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)

        // Test verb pagination
        mockMvc.perform(
            get("/api/v1/content/verbs")
                .param("page", "0")
                .param("size", "15")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
    }

    @Test
    fun `error handling should work for invalid parameters`() {
        // Test search with empty query should still work
        mockMvc.perform(
            get("/api/v1/content/dictionary/search")
                .param("q", "")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }
}
