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
import kotlin.test.assertEquals

class SentenceServiceTest {

    private lateinit var sentenceRepository: SentenceRepository
    private lateinit var sentenceService: SentenceService

    @BeforeEach
    fun setup() {
        sentenceRepository = mockk()
        sentenceService = SentenceService(sentenceRepository)
    }

    @Test
    fun `searchSentences should search by english and rif sentences`() {
        val query = "hello"
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        } returns mockResult

        val result = sentenceService.searchSentences(query, page, size)

        assertEquals(mockResult, result)
        verify { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `searchSentences should use default pagination parameters`() {
        val query = "test"
        val defaultPageable = PageRequest.of(0, 20)
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Test sentence", rifSentence = "Asekki n tarmid")
        ))
        every { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, defaultPageable) 
        } returns mockResult

        val result = sentenceService.searchSentences(query)

        assertEquals(mockResult, result)
        verify { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, defaultPageable) 
        }
    }

    @Test
    fun `searchSentences should handle empty results`() {
        val query = "nonexistent"
        val pageable = PageRequest.of(0, 20)
        val emptyResult = PageImpl(emptyList<Sentence>())
        every { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        } returns emptyResult

        val result = sentenceService.searchSentences(query)

        assertEquals(0, result.totalElements)
        assertEquals(emptyList(), result.content)
        verify { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `searchSentences should handle custom pagination`() {
        val query = "custom"
        val page = 2
        val size = 5
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(
            listOf(Sentence(id = "1", englishSentence = "Custom sentence", rifSentence = "Asekki amazigh")),
            pageable,
            15 // Total elements
        )
        every { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        } returns mockResult

        val result = sentenceService.searchSentences(query, page, size)

        assertEquals(15, result.totalElements)
        assertEquals(1, result.content.size)
        assertEquals(page, result.number)
        assertEquals(size, result.size)
        verify { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `getAllSentences should return all sentences with pagination`() {
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        ))
        every { sentenceRepository.findAll(pageable) } returns mockResult

        val result = sentenceService.getAllSentences(page, size)

        assertEquals(mockResult, result)
        assertEquals(2, result.content.size)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getAllSentences should use default pagination parameters`() {
        val defaultPageable = PageRequest.of(0, 20)
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Default sentence", rifSentence = "Asekki amezwaru")
        ))
        every { sentenceRepository.findAll(defaultPageable) } returns mockResult

        val result = sentenceService.getAllSentences()

        assertEquals(mockResult, result)
        verify { sentenceRepository.findAll(defaultPageable) }
    }

    @Test
    fun `getAllSentences should handle empty repository`() {
        val pageable = PageRequest.of(0, 20)
        val emptyResult = PageImpl(emptyList<Sentence>())
        every { sentenceRepository.findAll(pageable) } returns emptyResult

        val result = sentenceService.getAllSentences()

        assertEquals(0, result.totalElements)
        assertEquals(emptyList(), result.content)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getAllSentences should handle large page numbers`() {
        val page = 10
        val size = 50
        val pageable = PageRequest.of(page, size)
        val emptyResult = PageImpl(emptyList<Sentence>(), pageable, 100)
        every { sentenceRepository.findAll(pageable) } returns emptyResult

        val result = sentenceService.getAllSentences(page, size)

        assertEquals(100, result.totalElements)
        assertEquals(0, result.content.size)
        assertEquals(page, result.number)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomSentences should return random sample from pagination`() {
        val count = 10
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        )
        val mockPage = PageImpl(mockContent)
        every { sentenceRepository.findAll(pageable) } returns mockPage

        val result = sentenceService.getRandomSentences(count)

        assertEquals(mockContent, result)
        assertEquals(2, result.size)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomSentences should use default count of 10`() {
        val defaultPageable = PageRequest.of(0, 10)
        val mockContent = listOf(
            Sentence(id = "1", englishSentence = "Random sentence", rifSentence = "Asekki amazray")
        )
        val mockPage = PageImpl(mockContent)
        every { sentenceRepository.findAll(defaultPageable) } returns mockPage

        val result = sentenceService.getRandomSentences()

        assertEquals(mockContent, result)
        verify { sentenceRepository.findAll(defaultPageable) }
    }

    @Test
    fun `getRandomSentences should handle different count values`() {
        val count = 25
        val pageable = PageRequest.of(0, count)
        val mockContent = (1..15).map { 
            Sentence(id = "$it", englishSentence = "Sentence $it", rifSentence = "Asekki $it")
        }
        val mockPage = PageImpl(mockContent)
        every { sentenceRepository.findAll(pageable) } returns mockPage

        val result = sentenceService.getRandomSentences(count)

        assertEquals(15, result.size)
        assertEquals("Sentence 1", result[0].englishSentence)
        assertEquals("Sentence 15", result[14].englishSentence)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomSentences should handle empty repository`() {
        val count = 5
        val pageable = PageRequest.of(0, count)
        val emptyPage = PageImpl(emptyList<Sentence>())
        every { sentenceRepository.findAll(pageable) } returns emptyPage

        val result = sentenceService.getRandomSentences(count)

        assertEquals(emptyList(), result)
        verify { sentenceRepository.findAll(pageable) }
    }
}
