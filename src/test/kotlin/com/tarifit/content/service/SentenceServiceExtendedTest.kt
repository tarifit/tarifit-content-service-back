package com.tarifit.content.service

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.repository.sentence.SentenceRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SentenceServiceExtendedTest {

    private lateinit var sentenceRepository: SentenceRepository
    private lateinit var sentenceService: SentenceService

    @BeforeEach
    fun setup() {
        sentenceRepository = mockk()
        sentenceService = SentenceService(sentenceRepository)
    }

    @Test
    fun `searchByEnglish should search only English sentences`() {
        val query = "hello"
        val pageable = PageRequest.of(0, 20)
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Hello world", rifSentence = "Azul amadal")
        )
        val page = PageImpl(sentences)
        every { sentenceRepository.findByEnglishSentenceContainingIgnoreCase(query, pageable) } returns page

        val result = sentenceService.searchByEnglish(query)

        assertEquals(1, result.content.size)
        assertEquals("Hello world", result.content[0].englishSentence)
        verify { sentenceRepository.findByEnglishSentenceContainingIgnoreCase(query, pageable) }
    }

    @Test
    fun `searchByRif should search only Rif sentences`() {
        val query = "azul"
        val pageable = PageRequest.of(0, 20)
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        )
        val page = PageImpl(sentences)
        every { sentenceRepository.findByRifSentenceContainingIgnoreCase(query, pageable) } returns page

        val result = sentenceService.searchByRif(query)

        assertEquals(1, result.content.size)
        assertEquals("Azul", result.content[0].rifSentence)
        verify { sentenceRepository.findByRifSentenceContainingIgnoreCase(query, pageable) }
    }

    @Test
    fun `getSentenceById should return sentence when found`() {
        val id = "test-id"
        val sentence = Sentence(id = id, englishSentence = "Test", rifSentence = "Armad")
        every { sentenceRepository.findById(id) } returns Optional.of(sentence)

        val result = sentenceService.getSentenceById(id)

        assertEquals(sentence, result)
        verify { sentenceRepository.findById(id) }
    }

    @Test
    fun `getSentenceById should return null when not found`() {
        val id = "nonexistent"
        every { sentenceRepository.findById(id) } returns Optional.empty()

        val result = sentenceService.getSentenceById(id)

        assertNull(result)
        verify { sentenceRepository.findById(id) }
    }

    @Test
    fun `countAllSentences should return total count`() {
        val expectedCount = 150L
        every { sentenceRepository.count() } returns expectedCount

        val result = sentenceService.countAllSentences()

        assertEquals(expectedCount, result)
        verify { sentenceRepository.count() }
    }

    @Test
    fun `searchByEnglish should handle pagination`() {
        val query = "test"
        val page = 1
        val size = 5
        val pageable = PageRequest.of(page, size)
        val sentences = listOf(
            Sentence(id = "1", englishSentence = "Test sentence", rifSentence = "Asekki n tarmid")
        )
        val pageResult = PageImpl(sentences, pageable, 10)
        every { sentenceRepository.findByEnglishSentenceContainingIgnoreCase(query, pageable) } returns pageResult

        val result = sentenceService.searchByEnglish(query, page, size)

        assertEquals(10, result.totalElements)
        assertEquals(page, result.number)
        verify { sentenceRepository.findByEnglishSentenceContainingIgnoreCase(query, pageable) }
    }

    @Test
    fun `searchByRif should handle empty results`() {
        val query = "nonexistent"
        val pageable = PageRequest.of(0, 20)
        val emptyPage = PageImpl(emptyList<Sentence>())
        every { sentenceRepository.findByRifSentenceContainingIgnoreCase(query, pageable) } returns emptyPage

        val result = sentenceService.searchByRif(query)

        assertEquals(0, result.totalElements)
        assertEquals(emptyList(), result.content)
        verify { sentenceRepository.findByRifSentenceContainingIgnoreCase(query, pageable) }
    }
}
