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
        // Given
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

        // When
        val result = sentenceService.searchSentences(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { 
            sentenceRepository.findByEnglishOrRifSentenceContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `getAllSentences should return all sentences with pagination`() {
        // Given
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { sentenceRepository.findAll(pageable) } returns mockResult

        // When
        val result = sentenceService.getAllSentences(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { sentenceRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomSentences should return random sample from pagination`() {
        // Given
        val count = 10
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul"),
            Sentence(id = "2", englishSentence = "Goodbye", rifSentence = "Ar tufat")
        )
        val mockPage = PageImpl(mockContent)
        every { sentenceRepository.findAll(pageable) } returns mockPage

        // When
        val result = sentenceService.getRandomSentences(count)

        // Then
        assertEquals(mockContent, result)
        verify { sentenceRepository.findAll(pageable) }
    }
}
